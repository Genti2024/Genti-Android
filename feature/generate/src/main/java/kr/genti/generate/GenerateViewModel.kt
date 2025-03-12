package kr.genti.generate

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.genti.common.manager.ImageManager.getImageInfo
import kr.genti.domain.entity.request.ImageBucketRequestModel
import kr.genti.domain.entity.response.ImageBucketModel
import kr.genti.domain.entity.response.ImageFileModel
import kr.genti.domain.enums.FileType
import kr.genti.domain.enums.PictureNumber
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.repository.CreateRepository
import kr.genti.domain.repository.UploadRepository
import kr.genti.generate.model.GenerateStage
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
        val nextStage =
            generateState.value.currentStage.nextStage(generateState.value.pictureNumber)
        if (nextStage != GenerateStage.RESULT) {
            _generateState.update {
                it.copy(currentStage = nextStage, currentStep = it.currentStep + 1)
            }
        } else {
            viewModelScope.launch {
                sendImagesToGenerate()
            }
        }
    }

    private fun handleNumberSelect(pictureNumber: PictureNumber) {
        _generateState.update {
            it.copy(pictureNumber = pictureNumber)
        }
    }

    private fun handlePromptExampleSwipe() {
        // TODO : 앰플리튜드 작업
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
        viewModelScope.launch {
            _generateSideEffect.emit(GenerateSideEffect.StartImageSelect(isExtra))
        }
    }

    private fun handleImageSelect(uriList: List<Uri>) {
        _generateState.update {
            it.copy(imageList = uriList.map { uri ->
                val uriInfo = uri.getImageInfo()
                ImageFileModel(uriInfo.first, uriInfo.second, uriInfo.third)
            })
        }
    }

    private suspend fun getExamplePrompt() {
        createRepository.getPromptExample(
            getGenerateType(
                generateState.value.isParentPic,
                generateState.value.pictureNumber
            ).name
        ).onSuccess { result ->
            _generateState.update {
                it.copy(exampleList = result.toImmutableList())
            }
        }
    }

    private suspend fun sendImagesToGenerate() {
        runCatching {
            val imageBucketList = getThreeImageBucket(imageList = generateState.value.imageList)
            postThreeImage(imageBucketList)
        }
    }

    private suspend fun getThreeImageBucket(imageList: List<ImageFileModel>): List<ImageBucketModel> =
        createRepository.getThreeImageBucket(
            imageList.map { image ->
                ImageBucketRequestModel(FileType.USER_UPLOADED_IMAGE, image.name)
            }
        ).getOrThrow()

    private suspend fun postThreeImage(imageBucketList: List<ImageBucketModel>) {
        imageBucketList.mapIndexed { index, imageBucket ->
            async {
                uploadRepository.uploadImage(
                    preSignedURL = imageBucket.presignedUrl,
                    imageUri = generateState.value.imageList[index].url
                ).getOrThrow()
            }
        }.awaitAll()
    }
}