package kr.genti.presentation.main.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.genti.core.state.UiState
import kr.genti.domain.entity.request.CreateRequestModel
import kr.genti.domain.entity.request.KeyRequestModel
import kr.genti.domain.entity.request.S3RequestModel
import kr.genti.domain.entity.response.ImageFileModel
import kr.genti.domain.entity.response.ImageFileModel.Companion.emptyImageFileModel
import kr.genti.domain.entity.response.S3PresignedUrlModel
import kr.genti.domain.enums.CameraAngle
import kr.genti.domain.enums.FileType
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.enums.ShotCoverage
import kr.genti.domain.repository.CreateRepository
import kr.genti.domain.repository.UploadRepository
import kr.genti.domain.repository.UserRepository
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class CreateViewModel
    @Inject
    constructor(
        private val createRepository: CreateRepository,
        private val uploadRepository: UploadRepository,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        val prompt = MutableLiveData<String>()
        var plusImage = emptyImageFileModel()
        val isWritten = MutableLiveData(false)

        val selectedRatio = MutableLiveData<PictureRatio>()
        val selectedAngle = MutableLiveData<CameraAngle>()
        val selectedCoverage = MutableLiveData<ShotCoverage>()
        val isSelected = MutableLiveData(false)

        var imageList = listOf<ImageFileModel>()
        var isCompleted = MutableLiveData(false)

        private val _currentPercent = MutableStateFlow<Int>(33)
        val currentPercent: StateFlow<Int> = _currentPercent

        private var currentPrompt: String = ""

        private val _totalGeneratingState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
        val totalGeneratingState: StateFlow<UiState<Boolean>> = _totalGeneratingState

        private var uploadCheckList = mutableListOf(false, false, false, true)
        private var plusImageS3Key = KeyRequestModel(null)
        private var imageS3KeyList = listOf<KeyRequestModel>()

        fun modCurrentPercent(amount: Int) {
            _currentPercent.value += amount
        }

        fun checkWritten() {
            isWritten.value = prompt.value?.isNotEmpty()
        }

        fun selectRatio(item: PictureRatio) {
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

        fun getS3PresignedUrls() {
            _totalGeneratingState.value = UiState.Loading
            getSingleS3Url()
            getMultiUrls()
        }

        private fun getSingleS3Url() {
            if (plusImage.id != (-1).toLong()) {
                uploadCheckList[3] = false
                viewModelScope.launch {
                    createRepository.getS3SingleUrl(
                        S3RequestModel(
                            FileType.USER_UPLOADED_IMAGE,
                            plusImage.name,
                        ),
                    )
                        .onSuccess { uriModel ->
                            plusImageS3Key = KeyRequestModel(uriModel.s3Key)
                            postSingleImage(uriModel)
                        }.onFailure {
                            _totalGeneratingState.value = UiState.Failure(it.message.toString())
                        }
                }
            }
        }

        private fun getMultiUrls() {
            viewModelScope.launch {
                createRepository.getS3MultiUrl(
                    listOf(
                        S3RequestModel(FileType.USER_UPLOADED_IMAGE, imageList[0].name),
                        S3RequestModel(FileType.USER_UPLOADED_IMAGE, imageList[1].name),
                        S3RequestModel(FileType.USER_UPLOADED_IMAGE, imageList[2].name),
                    ),
                ).onSuccess { uriList ->
                    imageS3KeyList = uriList.map { KeyRequestModel(it.s3Key) }
                    postMultiImage(uriList)
                }.onFailure {
                    _totalGeneratingState.value = UiState.Failure(it.message.toString())
                }
            }
        }

        private fun postSingleImage(s3urlModel: S3PresignedUrlModel) {
            viewModelScope.launch {
                uploadRepository.uploadImage(s3urlModel.url, plusImage.url)
                    .onSuccess {
                        plusImageS3Key = KeyRequestModel(s3urlModel.s3Key)
                        uploadCheckList[3] = true
                        checkAllUploadFinished()
                    }.onFailure {
                        _totalGeneratingState.value = UiState.Failure(it.message.toString())
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
                            _totalGeneratingState.value = UiState.Failure(it.message.toString())
                            return@launch
                        }
                }
            }
        }

        // TODO: plusImageS3Key nullable로 수정
        private fun checkAllUploadFinished() {
            if (uploadCheckList.all { it }) {
                viewModelScope.launch {
                    createRepository.postToCreate(
                        CreateRequestModel(
                            prompt.value ?: return@launch,
                            plusImageS3Key,
                            imageS3KeyList,
                            selectedAngle.value ?: return@launch,
                            selectedCoverage.value ?: return@launch,
                            selectedRatio.value ?: return@launch,
                        ),
                    )
                        .onSuccess {
                            _totalGeneratingState.value = UiState.Success(it)
                        }.onFailure {
                            _totalGeneratingState.value = UiState.Failure(it.message.toString())
                        }
                }
            }
        }

        fun resetGeneratingState() {
            _totalGeneratingState.value = UiState.Empty
        }

        fun getIsGuideNeeded() = userRepository.getIsGuideNeeded()

        fun setIsGuideNeeded(isGuideNeeded: Boolean) = userRepository.setIsGuideNeeded(isGuideNeeded)

        fun getRandomPrompt(): String {
            val randomPrompt = promptList[Random.nextInt(promptList.size)]
            if (randomPrompt != currentPrompt) currentPrompt = randomPrompt
            return currentPrompt
        }

        companion object {
            val promptList =
                listOf(
                    "프랑스 야경을 즐기는 모습을 그려주세요. 항공점퍼를 입고 테라스에 서 있는 모습이에요",
                    "시간 대는 밤, 장소는 야경이 이쁘게, 의상은 그냥 캐주얼한 옷. 포즈는 자연스럽게 걷는 자세",
                    "산 속에서 캠핑카 앞에서 음악 듣고 있는 모습. 배경은 캠핑카이고 자연스러운 캠핑 복장 입고 있으면 좋겠음",
                    "배경은 저녁 파리에서 옷은 검정 항공잠바와 검정 조거 팬츠를 입고 자세는 서서 구경하는 모습이면 좋겠어",
                    "멋진 턱시도 정장을 입고 결혼식에서 신랑 입장을 하고 있는 제 모습을 만들어보고 싶어요.",
                    "배경은 거리의 노란 가로등 아래이고, 저는 우산을 접고 영화처럼 춤을 추고 있어요",
                    "배경은 캠핑장이고, 저는 텐트 옆에 앉아있고 캠프 파이어 옆에 마시멜로우를 굽고 있어요",
                )
        }
    }
