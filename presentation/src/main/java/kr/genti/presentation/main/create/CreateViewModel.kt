package kr.genti.presentation.main.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.genti.core.state.UiState
import kr.genti.domain.entity.request.GenerateRequestModel
import kr.genti.domain.entity.request.S3RequestModel
import kr.genti.domain.entity.response.ImageFileModel
import kr.genti.domain.entity.response.PromptModel
import kr.genti.domain.entity.response.S3PresignedUrlModel
import kr.genti.domain.entity.response.emptyImageFileModel
import kr.genti.domain.enums.CameraAngle
import kr.genti.domain.enums.FileType
import kr.genti.domain.enums.ImageRatio
import kr.genti.domain.enums.ShotCoverage
import kr.genti.domain.repository.CreateRepository
import kr.genti.domain.repository.UploadRepository
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CreateViewModel
    @Inject
    constructor(
        private val createRepository: CreateRepository,
        private val uploadRepository: UploadRepository,
    ) : ViewModel() {
        val prompt = MutableLiveData<String>()
        var plusImage = emptyImageFileModel()
        val isWritten = MutableLiveData(false)

        val selectedRatio = MutableLiveData<ImageRatio>()
        val selectedAngle = MutableLiveData<CameraAngle>()
        val selectedCoverage = MutableLiveData<ShotCoverage>()
        val isSelected = MutableLiveData(false)

        var imageList = listOf<ImageFileModel>()
        var isCompleted = MutableLiveData(false)

        private val _currentPercent = MutableStateFlow<Int>(33)
        val currentPercent: StateFlow<Int> = _currentPercent

        private var examplePromptList = listOf<PromptModel>()
        private var currentPromptId: Long = -1

        private val _getExamplePromptsResult = MutableSharedFlow<Boolean>()
        val getExamplePromptsResult: SharedFlow<Boolean> = _getExamplePromptsResult

        private val _getRandomPromptState = MutableStateFlow<UiState<PromptModel>>(UiState.Empty)
        val getRandomPromptState: StateFlow<UiState<PromptModel>> = _getRandomPromptState

        private val _totalGeneratingResult = MutableSharedFlow<Boolean>()
        val totalGeneratingResult: SharedFlow<Boolean> = _totalGeneratingResult

        private var uploadCheckList = mutableListOf(false, false, false, true)
        private var plusImageS3Key: String? = null
        private var imageS3KeyList = listOf<String>()

        init {
            getExamplePromptsFromServer()
        }

        fun modCurrentPercent(amount: Int) {
            _currentPercent.value += amount
        }

        fun checkWritten() {
            isWritten.value = prompt.value?.isNotEmpty()
        }

        fun selectRatio(item: ImageRatio) {
            selectedRatio.value = item
            checkSelected()
        }

        fun selectAngle(item: CameraAngle) {
            selectedAngle.value = item
            checkSelected()
        }

        fun selectFrame(item: ShotCoverage) {
            selectedCoverage.value = item
            checkSelected()
        }

        private fun checkSelected() {
            isSelected.value =
                selectedRatio.value != null && selectedAngle.value != null && selectedCoverage.value != null
        }

        private fun getExamplePromptsFromServer() {
            if (examplePromptList.isEmpty()) {
                viewModelScope.launch {
                    createRepository.getExamplePrompts()
                        .onSuccess {
                            _getExamplePromptsResult.emit(true)
                            examplePromptList = it
                            getRandomPrompt()
                        }
                        .onFailure {
                            _getExamplePromptsResult.emit(false)
                        }
                }
            }
        }

        fun getRandomPrompt() {
            val filteredList = examplePromptList.filter { it.id != currentPromptId }
            if (filteredList.isNotEmpty()) {
                val randomPrompt = filteredList[Random.nextInt(filteredList.size)]
                currentPromptId = randomPrompt.id
                _getRandomPromptState.value = UiState.Success(randomPrompt)
            } else {
                _getRandomPromptState.value = UiState.Failure(currentPromptId.toString())
            }
        }

        fun getS3PresignedUrls() {
            if (plusImage.id != (-1).toLong()) {
                uploadCheckList[3] = false
                viewModelScope.launch {
                    createRepository.getS3SingleUrl(
                        S3RequestModel(
                            plusImage.name,
                            FileType.USER_UPLOADED_IMAGE,
                        ),
                    )
                        .onSuccess { uriModel ->
                            plusImageS3Key = uriModel.s3Key
                            postSingleImage(uriModel)
                        }.onFailure {
                            _totalGeneratingResult.emit(false)
                        }
                }
            }
            viewModelScope.launch {
                createRepository.getS3MultiUrl(
                    listOf(
                        S3RequestModel(imageList[0].name, FileType.USER_UPLOADED_IMAGE),
                        S3RequestModel(imageList[1].name, FileType.USER_UPLOADED_IMAGE),
                        S3RequestModel(imageList[2].name, FileType.USER_UPLOADED_IMAGE),
                    ),
                ).onSuccess { uriList ->
                    imageS3KeyList = uriList.map { it.s3Key }
                    postMultiImage(uriList)
                }.onFailure {
                    _totalGeneratingResult.emit(false)
                }
            }
        }

        private fun postSingleImage(s3urlModel: S3PresignedUrlModel) {
            viewModelScope.launch {
                uploadRepository.uploadImage(s3urlModel.url, plusImage.url)
                    .onSuccess {
                        plusImageS3Key = s3urlModel.s3Key
                        uploadCheckList[3] = true
                        checkAllUploadFinished()
                    }.onFailure {
                        _totalGeneratingResult.emit(false)
                    }
            }
        }

        private fun postMultiImage(s3urlList: List<S3PresignedUrlModel>) {
            viewModelScope.launch {
                for (i in 0..2) {
                    uploadRepository.uploadImage(s3urlList[i].url, imageList[i].url)
                        .onSuccess {
                            uploadCheckList[i] = true
                            if (i == 2) checkAllUploadFinished()
                        }.onFailure {
                            _totalGeneratingResult.emit(false)
                            return@launch
                        }
                }
            }
        }

        // TODO: request 수정
        private fun checkAllUploadFinished() {
            if (uploadCheckList.all { it }) {
                viewModelScope.launch {
                    createRepository.postToGenerate(
                        GenerateRequestModel(
                            prompt.value ?: return@launch,
                            plusImageS3Key ?: return@launch,
                            imageS3KeyList ?: return@launch,
                            selectedAngle.value ?: return@launch,
                            selectedCoverage.value ?: return@launch,
                        ),
                    )
                        .onSuccess {
                            _totalGeneratingResult.emit(true)
                        }.onFailure {
                            _totalGeneratingResult.emit(false)
                        }
                }
            }
        }
    }
