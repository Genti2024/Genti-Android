package kr.genti.generate

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.genti.common.manager.AmplitudeManager
import kr.genti.common.manager.AmplitudeManager.EVENT_CLICK_BTN
import kr.genti.common.manager.AmplitudeManager.PROPERTY_BTN
import kr.genti.common.manager.AmplitudeManager.PROPERTY_PAGE
import kr.genti.common.manager.ImageManager.getImageInfo
import kr.genti.domain.entity.request.CreateRequestModel
import kr.genti.domain.entity.request.CreateTwoRequestModel
import kr.genti.domain.entity.request.ImageBucketRequestModel
import kr.genti.domain.entity.request.KeyRequestModel
import kr.genti.domain.entity.response.ImageBucketModel
import kr.genti.domain.entity.response.ImageFileModel
import kr.genti.domain.enums.FileType
import kr.genti.domain.enums.PictureNumber
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.repository.CreateRepository
import kr.genti.domain.repository.UploadRepository
import kr.genti.generate.model.GenerateStage
import kr.genti.generate.model.GenerateType
import kr.genti.generate.model.GenerateType.Companion.getGenerateType
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel
@Inject
constructor(
    private val createRepository: CreateRepository,
    private val uploadRepository: UploadRepository,
) : ViewModel() {
    private val _generateState = MutableStateFlow(GenerateState())
    val generateState = _generateState.asStateFlow()

    private val _generateSideEffect = MutableSharedFlow<GenerateSideEffect>()
    val generateSideEffect = _generateSideEffect.asSharedFlow()

    fun onIntent(intent: GenerateIntent) {
        when (intent) {
            is GenerateIntent.Init -> handleInit(intent.isParentPic)
            is GenerateIntent.BackBtnClick -> handleBackBtnClick()
            is GenerateIntent.NextBtnClick -> handleNextBtnClick()
            is GenerateIntent.NumberSelect -> handleNumberSelect(intent.pictureNumber)
            is GenerateIntent.PromptExampleSwipe -> handlePromptExampleSwipe()
            is GenerateIntent.PromptChange -> handlePromptChange(intent.prompt)
            is GenerateIntent.TextFieldFocused -> handleTextFieldOutsideClick(intent.isFocused)
            is GenerateIntent.RatioSelect -> handleRatioSelect(intent.pictureRatio)
            is GenerateIntent.ImageSelectBtnClick -> handleImageSelectBtnClick(intent.isExtra)
            is GenerateIntent.ImageSelect -> handleImageSelect(intent.uriList)
        }
    }

    private fun handleInit(isParentPic: Boolean) {
        _generateState.update {
            it.copy(
                isParentPic = isParentPic,
                currentStep = 1,
                currentStage = if (isParentPic) GenerateStage.NUMBER_SELECT else GenerateStage.PROMPT_INPUT
            )
        }
        viewModelScope.launch {
            getExamplePrompt()
        }
    }

    private fun handleBackBtnClick() {
        val prevStage =
            generateState.value.currentStage.prevStage(generateState.value.isParentPic)
        if (prevStage != GenerateStage.INIT) {
            _generateState.update {
                it.copy(currentStage = prevStage, currentStep = it.currentStep - 1)
            }
        } else {
            viewModelScope.launch {
                _generateSideEffect.emit(GenerateSideEffect.NavigateToBack)
            }
        }
    }

    private fun handleNextBtnClick() {
        amplitudeTrackNextButton(generateState.value.currentStage)
        val nextStage =
            generateState.value.currentStage.nextStage(generateState.value.pictureNumber)
        if (nextStage != GenerateStage.RESULT) {
            _generateState.update {
                it.copy(currentStage = nextStage, currentStep = it.currentStep + 1)
            }
        } else {
            amplitudeTrackStartCreate()
            viewModelScope.launch {
                changeLoadingState(true)
                requestGenerate()
                changeLoadingState(false)
            }
        }
    }

    private fun handleNumberSelect(pictureNumber: PictureNumber) {
        _generateState.update {
            it.copy(pictureNumber = pictureNumber)
        }
    }

    private fun handlePromptExampleSwipe() {
        amplitudeTrackExampleSwipe()
    }

    private fun handlePromptChange(prompt: String) {
        _generateState.update {
            it.copy(prompt = prompt)
        }
    }

    private fun handleTextFieldOutsideClick(isFocusClearNeeded: Boolean) {
        _generateState.update {
            it.copy(isFocusClearNeeded = isFocusClearNeeded)
        }
    }

    private fun handleRatioSelect(pictureRatio: PictureRatio) {
        _generateState.update {
            it.copy(pictureRatio = pictureRatio)
        }
    }

    private fun handleImageSelectBtnClick(isExtra: Boolean) {
        _generateState.update {
            it.copy(isSelectingExtra = isExtra)
        }
        viewModelScope.launch {
            _generateSideEffect.emit(GenerateSideEffect.StartImageSelect)
        }
    }

    private fun handleImageSelect(uriList: List<Uri>) {
        val selectedImageList = uriList.map { uri ->
            val uriInfo = uri.getImageInfo()
            ImageFileModel(uriInfo.first, uriInfo.second, uriInfo.third)
        }
        _generateState.update {
            amplitudeTrackSelectImage(it.isSelectingExtra)
            if (!it.isSelectingExtra) {
                it.copy(imageList = selectedImageList)
            } else {
                it.copy(extraImageList = selectedImageList)
            }
        }
    }

    private fun changeLoadingState(isLoading: Boolean) {
        _generateState.update {
            it.copy(isLoading = isLoading)
        }
    }

    /** 프롬프트뷰 예시 이미지 리스트 관련*/

    private suspend fun getExamplePrompt() {
        val generateType =
            getGenerateType(generateState.value.isParentPic, generateState.value.pictureNumber)
        createRepository.getPromptExample(generateType.name)
            .onSuccess { result ->
                amplitudeTrackViewExample(generateType)
                _generateState.update {
                    it.copy(exampleList = result.toImmutableList())
                }
            }
    }

    /** 이미지 생성 요청 관련*/

    private suspend fun requestGenerate() {
        runCatching {
            if (generateState.value.pictureNumber != PictureNumber.TWO) {
                postThreeImageToGenerate()
            } else {
                postSixImageToGenerate()
            }
        }.onSuccess {
            amplitudeTrackFinishCreate()
            _generateSideEffect.emit(GenerateSideEffect.NavigateToWaiting)
        }.onFailure {
            _generateSideEffect.emit(GenerateSideEffect.ShowErrorToast)
        }
    }

    private suspend fun postThreeImageToGenerate() {
        val keyList = uploadThreeImage(generateState.value.imageList)
        val request = CreateRequestModel(
            generateState.value.prompt, keyList, generateState.value.pictureRatio,
        )
        if (!generateState.value.isParentPic) {
            createRepository.postToCreate(request).getOrThrow()
        } else {
            createRepository.postToCreateOne(request).getOrThrow()
        }
    }

    private suspend fun postSixImageToGenerate() = coroutineScope {
        val keyList = listOf(
            async { uploadThreeImage(generateState.value.imageList) },
            async { uploadThreeImage(generateState.value.extraImageList) }
        ).awaitAll()
        val request = CreateTwoRequestModel(
            generateState.value.prompt, keyList[0], keyList[1], generateState.value.pictureRatio
        )
        createRepository.postToCreateTwo(request).getOrThrow()
    }

    /** 이미지 3장 AWS S3 업로드 관련*/

    private suspend fun uploadThreeImage(selectedImageList: List<ImageFileModel>): List<KeyRequestModel> {
        val imageBucketList = getThreeImageBucket(selectedImageList)
        uploadThreeImageToBucket(imageBucketList, selectedImageList)
        return imageBucketList.map { KeyRequestModel(it.s3Key) }
    }

    private suspend fun getThreeImageBucket(selectedImageList: List<ImageFileModel>): List<ImageBucketModel> =
        createRepository.getThreeImageBucket(
            selectedImageList.map { image ->
                ImageBucketRequestModel(FileType.USER_UPLOADED_IMAGE, image.name)
            }
        ).getOrThrow()

    private suspend fun uploadThreeImageToBucket(
        imageBucketList: List<ImageBucketModel>,
        selectedImageList: List<ImageFileModel>
    ) = coroutineScope {
        imageBucketList.mapIndexed { index, imageBucket ->
            async {
                uploadRepository.uploadImage(
                    imageBucket.presignedUrl, selectedImageList[index].url
                ).getOrThrow()
            }
        }.awaitAll()
    }

    /** 앰플리튜드 관련*/

    private fun amplitudeTrackNextButton(currentStage: GenerateStage) {
        val stage = when (currentStage) {
            GenerateStage.NUMBER_SELECT -> "create0"
            GenerateStage.PROMPT_INPUT -> "create1"
            GenerateStage.RATIO_SELECT -> "create2"
            else -> ""
        }
        if (stage.isEmpty()) return
        AmplitudeManager.trackEvent(
            EVENT_CLICK_BTN,
            mapOf(PROPERTY_PAGE to stage),
            mapOf(PROPERTY_BTN to "next"),
        )
        if (currentStage == GenerateStage.RATIO_SELECT) {
            when (generateState.value.pictureNumber) {
                PictureNumber.ONE -> AmplitudeManager.trackEvent("view_createoneparent")
                PictureNumber.TWO -> AmplitudeManager.trackEvent("view_createtwoparents")
                else -> return
            }
        }
    }

    private fun amplitudeTrackExampleSwipe() {
        AmplitudeManager.apply {
            trackEvent(
                EVENT_CLICK_BTN,
                mapOf(PROPERTY_PAGE to "create1"),
                mapOf(PROPERTY_BTN to "promptsuggest_refresh"),
            )
            plusIntProperties("user_promptsuggest_refresh")
        }
    }

    private fun amplitudeTrackViewExample(generateType: GenerateType) {
        when (generateType) {
            GenerateType.PAID_ONE -> AmplitudeManager.trackEvent("view_oneparentpreset")
            GenerateType.PAID_TWO -> AmplitudeManager.trackEvent("view_twoparentspreset")
            else -> return
        }
    }

    private fun amplitudeTrackSelectImage(isExtra: Boolean) {
        AmplitudeManager.trackEvent(
            EVENT_CLICK_BTN,
            mapOf(PROPERTY_PAGE to "create3"),
            mapOf(PROPERTY_BTN to if (!isExtra) "selectpic1" else "selectpic2")
        )
    }

    private fun amplitudeTrackStartCreate() {
        AmplitudeManager.trackEvent(
            EVENT_CLICK_BTN, mapOf(PROPERTY_PAGE to "create3"), mapOf(PROPERTY_BTN to "createpic"),
        )
    }

    private fun amplitudeTrackFinishCreate() {
        when (generateState.value.pictureNumber) {
            PictureNumber.NONE -> AmplitudeManager.plusIntProperties("user_piccreate_original")
            PictureNumber.ONE -> AmplitudeManager.trackEvent("complete_oneparent")
            PictureNumber.TWO -> AmplitudeManager.trackEvent("complete_twoparents")
        }
        AmplitudeManager.plusIntProperties("user_piccreate_total")
    }
}