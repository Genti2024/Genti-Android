package kr.genti.presentation.main.create

import android.net.Uri
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
import kr.genti.domain.entity.response.PromptModel
import kr.genti.domain.entity.response.S3PresignedUrlModel
import kr.genti.domain.enums.FileType
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
        var plusImage: Uri = Uri.EMPTY
        val isWritten = MutableLiveData(false)

        val selectedRatio = MutableLiveData<Int>(-1)
        val selectedAngle = MutableLiveData<Int>(-1)
        val selectedCoverage = MutableLiveData<Int>(-1)
        val isSelected = MutableLiveData(false)

        var uriList = listOf<Uri>()
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

        fun selectRatio(itemId: Int) {
            selectedRatio.value = itemId
            checkSelected()
        }

        fun selectAngle(itemId: Int) {
            selectedAngle.value = itemId
            checkSelected()
        }

        fun selectFrame(itemId: Int) {
            selectedCoverage.value = itemId
            checkSelected()
        }

        private fun checkSelected() {
            isSelected.value =
                selectedRatio.value != -1 && selectedAngle.value != -1 && selectedCoverage.value != -1
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
            // TODO: 파일명 대응
            if (plusImage != Uri.EMPTY) {
                uploadCheckList[3] = false
                viewModelScope.launch {
                    createRepository.getS3SingleUrl(
                        S3RequestModel(
                            "sangho1.jpg",
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
                        S3RequestModel("sangho2.jpg", FileType.USER_UPLOADED_IMAGE),
                        S3RequestModel("sangho3.jpg", FileType.USER_UPLOADED_IMAGE),
                        S3RequestModel("sangho4.jpg", FileType.USER_UPLOADED_IMAGE),
                    ),
                ).onSuccess { uriList ->
                    imageS3KeyList = uriList.map { it.s3Key }
                    _totalGeneratingResult.emit(true)
                    postMultiImage(uriList)
                }.onFailure {
                    _totalGeneratingResult.emit(false)
                }
            }
        }

        private fun postSingleImage(uriModel: S3PresignedUrlModel) {
            viewModelScope.launch {
                uploadRepository.uploadImage(uriModel.url, plusImage.toString())
                    .onSuccess {
                        plusImageS3Key =
                            checkAllUploadFinished()
                    }.onFailure {
                        _totalGeneratingResult.emit(false)
                    }
            }
        }

        private fun postMultiImage(uriList: List<S3PresignedUrlModel>) {
            viewModelScope.launch {
                for (i in 0..2) {
                    uploadRepository.uploadImage(uriList[i].url, plusImage.toString())
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

        private fun checkAllUploadFinished() {
            if (uploadCheckList.all { true }) {
                viewModelScope.launch {
                    createRepository.postToGenerate(
                        GenerateRequestModel(
                            prompt,
                            plusImageS3Key,
                            imageS3KeyList,
                            selectedAngle,
                            selectedCoverage,
                        ),
                    )
                }
            }
        }
    }
