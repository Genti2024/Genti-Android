package kr.genti.presentation.main.create

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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

        fun startSendingImages() {
            _totalGeneratingState.value = UiState.Loading
            viewModelScope.launch {
                runCatching {
                    listOfNotNull(
                        if (plusImage.id != (-1).toLong()) async { getSingleS3Url() } else null,
                        async { getMultiS3Urls() },
                    ).awaitAll()
                }.onSuccess {
                    postToGenerateImage()
                }.onFailure {
                    _totalGeneratingState.value = UiState.Failure(it.message.toString())
                }
            }
        }

        private suspend fun getSingleS3Url() {
            createRepository.getS3SingleUrl(
                S3RequestModel(FileType.USER_UPLOADED_IMAGE, plusImage.name),
            )
                .onSuccess { uriModel ->
                    plusImageS3Key = KeyRequestModel(uriModel.s3Key)
                    postSingleImage(uriModel)
                }.onFailure {
                    _totalGeneratingState.value = UiState.Failure(it.message.toString())
                }
        }

        private suspend fun getMultiS3Urls() {
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

        private suspend fun postSingleImage(urlModel: S3PresignedUrlModel) {
            uploadRepository.uploadImage(urlModel.url, plusImage.url)
                .onFailure {
                    _totalGeneratingState.value = UiState.Failure(it.message.toString())
                }
        }

        private suspend fun postMultiImage(urlModelList: List<S3PresignedUrlModel>) {
            urlModelList.mapIndexed { i, urlModel ->
                viewModelScope.async {
                    uploadRepository.uploadImage(urlModel.url, imageList[i].url)
                        .onFailure {
                            _totalGeneratingState.value = UiState.Failure(it.message.toString())
                        }
                }
            }.awaitAll()
        }

        private fun postToGenerateImage() {
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
                    "어두운 야간에 도시 골목의 횡단보도를 따라 빨간 미니 드레스를 입고 걸어가는 모습을 만들어주세요",
                    "초록색 숲 속에서 등산복을 입고, 두 팔을 벌리고 있는 모습을 만들어주세요",
                    "밤하늘에 반짝이는 별들 아래에서 검은 가운을 입고, 두 손을 모아 소망을 비는 사진을 만들어주세요",
                    "황혼이 짙어지는 사막에서 회색 후드티를 입고, 모래 위에 걸터앉아 먼 곳을 바라보는 모습을 만들어주세요",
                    "도심 속 번화가에서 하얀 셔츠와 검은 슬랙스를 입고, 한 손에는 커피잔을 들고 있는 사진을 만들어주세요",
                    "사람 없는 성당에서 사제복을 입고 혼자 앉아 기도하는 모습을 만들어주세요",
                    "고요한 호수 위에 있는 작은 보트 안에서 청록색 비옷을 입고, 손에는 낚싯대를 들고 있는 모습을 만들어주세요",
                    "노을진 해안가에서 파란 원피스를 입고, 바다에 발을 담그고 있는 모습을 만들어주세요",
                    "황금빛 들녘에서 터틀넥 니트를 입고, 햇볕을 받고 있는 모습을 만들어주세요",
                    "야생화가 만발한 초원에서 보라색 원피스를 입고, 잔디 위에 앉아 있는 사진을 만들어주세요",
                    "고요한 호수 옆에서 연두색 래쉬가드를 입고 점프하는 모습을 만들어주세요",
                    "가을 숲에서 오렌지색 후드와 갈색 부츠를 신고, 단풍을 바라보는 모습을 만들어주세요",
                    "도심 골목에서 회색 트렌치코트를 입고 우산을 쓰고 걷는 모습을 만들어주세요",
                    "눈이 내리는 고산지대에서 청록색 패딩 점퍼와 빨간 비니를 쓰고, 눈을 맞고 있는 사진을 만들어주세요",
                    "꽃이 만발한 정원에서 노란 민소매 원피스를 입고, 꽃을 따고 있는 모습을 만들어주세요",
                    "산 너머 해가 지는 노을을 바라보며 보라색 니트를 입고 손을 모아 기도하는 모습을 만들어주세요",
                    "도심 골목에서 검은색 가죽 재킷을 입고 벽에 기대어 있는 사진을 만들어주세요",
                    "북극에서 패딩 재킷을 입고 썰매를 타는 모습을 만들어주세요",
                    "벚꽃이 만발한 공원에서 연보라색 가디건을 입고 꽃 아래에 앉아 있는 사진을 만들어주세요",
                    "호화로운 연회장에서 금색 드레스를 입고 유리잔을 들고 있는 모습을 만들어주세요",
                    "황금빛 해변에서 흰색 민소매 원피스를 입고 파도에 발을 담그는 사진을 만들어주세요",
                    "모래 사막을 가로지르며 갈색 조끼와 모자를 쓰고 북극성을 따라가는 모습을 만들어주세요",
                    "늦은 가을 날씨에 짙은 보라색 니트와 잔디색 스커트를 입고 단풍길을 걷는 사진을 만들어주세요",
                    "시내 중심가에서 회색 셔츠와 검은 바지를 입고 스마트폰을 보는 모습을 만들어주세요",
                    "산등성이를 올라가면서 등산복을 입고 높은 곳을 향해 발을 디디는 모습을 만들어주세요",
                    "비바람이 부는 바닷가에서 검은 바람막이를 입고 손에 모래를 쥐며 바다를 바라보는 사진을 만들어주세요",
                    "호텔 로비에서 하얀 셔츠와 회색 정장을 입고 커피잔을 들고 앉아 있는 모습을 만들어주세요",
                    "어두운 숲 속에서 검은색 망토를 입고, 마법 지팡이를 휘두르는 마법사의 모습을 만들어주세요",
                    "도심 한복판에서 파란색 스포츠 재킷을 입고, 자전거를 타고 지나가는 모습을 만들어주세요",
                    "해안가에서 파도 소리를 들으며 책을 읽는 모습을 만들어주세요",
                    "이국적인 해변에서 하얀 원피스를 입고 걸어가는 사진을 만들어주세요",
                    "흙길을 따라 걸으면서 갈색 트래킹화를 신고 자연을 즐기는 모습을 만들어주세요",
                    "울창한 숲 속에서 갈색 무릎 담요를 두르고 책을 읽는 모습을 만들어주세요",
                    "빨간색 스노우복을 입고 스노우보드를 타면서 눈 위를 휘몰아치는 모습을 만들어주세요",
                    "옛날 도서관에서 베이지색 가디건을 입고 독서 중인 모습을 만들어주세요",
                    "도심 속 카페에서 회색 스웨터와 청바지를 입고, 커피잔을 들고 앉아 있는 모습을 만들어주세요",
                    "빙하 위에서 털달린 점퍼를 입고 당당히 서 있는 모습을 만들어주세요",
                    "산책로에서 노란 트레일러 재킷을 입고, 반려견과 함께 산책하는 모습을 만들어주세요",
                    "연두색 블라우스와 청바지를 입고 강가를 걷는 모습을 만들어주세요",
                    "해가 지는 해변가에서 보라색 선글라스를 쓰고 바다를 즐기는 모습을 만들어주세요",
                    "정원에서 하얀 드레스를 입고 꽃 사이를 걸어가는 모습을 만들어주세요",
                    "흰색 반팔과 청바지를 입고 공원에서 나들이하는 모습을 만들어주세요",
                    "해적 선박 위에서 붉은색 터틀넥을 입고, 나침반을 들고 항해하는 모습을 만들어주세요",
                    "우주 정거장에서 우주복을 입고 전화를 거는 모습을 만들어주세요",
                    "동굴 속에서 동물 가죽옷을 입고 불을 피우는 사냥꾼의 모습을 만들어주세요",
                    "미래 도시에서 은색 모자와 안경을 쓰고 로봇을 타고 있는 모습을 만들어주세요",
                    "판타지 세계에서 화려한 마법 로브를 입고 마법진을 그리는 모습을 만들어주세요",
                    "로맨틱한 성에서 고딕 스타일 드레스를 입고 태양이 뜨는 것을 기다리는 모습을 만들어주세요",
                    "고대 신전에서 황금색 로브를 입고 예배를 드리는 모습을 만들어주세요",
                    "정글 속에서 군복을 입고 숨어있는 모습을 만들어주세요",
                    "버섯으로 가득한 숲에서 보라색 망토를 두르고 요정들을 만나는 모습을 만들어주세요",
                    "중세시대 성에서 화려한 왕관과 드레스를 입고 왕자처럼 앉아 있는 모습을 만들어주세요",
                    "은밀한 암시장에서 검은 후드와 망토를 입고 비밀스러운 거래를 하는 모습을 만들어주세요",
                    "파리의 에펠탑 앞에서 모던한 프랑스 스타일의 옷을 입고 사진을 찍는 모습을 만들어주세요",
                    "이탈리아의 피렌체에서 토스카나 와인을 마시며 토마토 소스 파스타를 즐기는 모습을 만들어주세요",
                    "일본의 교토에서 전통적인 기모노를 입고 기와집에서 차를 마시며 휴식을 즐기는 모습을 만들어주세요",
                    "뉴욕의 타임스퀘어에서 화려한 광고판 아래에서 스트리트 푸드를 먹으며 번화가를 거닐고 있는 모습을 만들어주세요",
                    "독일의 베를린에서 커피와 프레첼을 즐기며 베를린의 벽 옆에 앉아있는 모습을 만들어주세요",
                    "런던의 버킹엄 궁전 앞에서 영국 스타일의 코트와 모자를 착용하고 왕가의 궁전을 구경하는 모습을 만들어주세요",
                    "중국 전통복을 입고 만리장성을 배경으로 사진을 찍고 있는 모습을 만들어주세요",
                    "태국의 방콕에서 태국 전통복을 입고 왕궁을 찾아다니며 문화를 체험하는 모습을 만들어주세요",
                    "서울의 경복궁에서 한복을 입고 전통 차를 마시며 한국의 역사를 체험하는 모습을 만들어주세요",
                    "이집트의 피라미드에서 사막 옷을 입고 낙타를 타고 모래사장을 달리는 모습을 만들어주세요",
                    "이스탄불에서 터키 전통 의상을 입고 역사적인 건물을 구경하는 모습을 만들어주세요",
                    "싱가포르 호텔에서 고급스러운 파티 의상을 입고 루프탑 바에서 세련된 칵테일을 즐기는 모습을 만들어주세요",
                    "멜버른에서 힙한 스트릿 패션을 입고 커피 샵에서 브런치를 즐기며 카페에서 시간을 보내는 모습을 만들어주세요",
                    "앙코르와트에서 탐험복을 입고 거대한 석조 건축물을 배경으로 사진을 찍는 모습을 만들어주세요",
                    "캐나다의 밴쿠버에서 모피 재킷과 부츠를 입고 산책을 즐기며 자연을 만끽하는 모습을 만들어주세요",
                    "공원에서 운동복을 입고 조깅을 하는 모습을 만들어주세요",
                    "카페에서 편한 옷을 입고 의자에 앉아 커피를 마시는 모습을 만들어주세요",
                    "학교에서 교복을 입고 책가방을 들고 수업에 참여하는 학생 모습을 만들어주세요",
                    "공원에서 반팔 티셔츠와 반바지를 입고 캐치볼을 하며 노는 모습을 만들어주세요",
                    "슈퍼마켓에서 일상적인 옷을 입고 장을 보는 모습을 만들어주세요",
                    "놀이공원에서 롤러코스터를 타는 모습을 만들어주세요",
                    "해변에서 모래사장에 누워 일광욕을 하며 책을 읽는 모습을 만들어주세요",
                    "영화관에서 팝콘을 먹으면서 영화를 보는 모습을 만들어주세요",
                    "클럽에서 원피스를 입고 춤추는 모습을 만들어주세요",
                    "동물원에서 귀여운 동물들을 보며 놀라는 모습을 만들어주세요",
                    "몽골의 사막에서 거대한 토끼를 타고 별 아래 있는 모습을 만들어주세요",
                    "고대 로마의 지하 예배당에서 파티를 열고 디제잉하는 모습을 만들어주세요",
                    "동물원을 배경으로 노란 멜빵바지를 입은채 다람쥐를 손에 올리고 웃고 있는 모습을 만들어주세요",
                    "화려한 꽃무늬 원피스를 입고 커다란 종이비행기를 타고 하늘 위에 서서 춤을 추고 있는 모습을 만들어주세요",
                )
        }
    }
