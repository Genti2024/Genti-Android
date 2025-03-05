package kr.genti.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.genti.common.manager.AmplitudeManager
import kr.genti.domain.entity.response.FeedItemModel
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.repository.FeedRepository
import javax.inject.Inject

@HiltViewModel
class FeedViewModel
@Inject
constructor(
    private val feedRepository: FeedRepository,
) : ViewModel() {
    private val _feedState = MutableStateFlow(FeedState())
    val feedState = _feedState.asStateFlow()

    private val _feedSideEffect = MutableSharedFlow<FeedSideEffect>()
    val feedSideEffect = _feedSideEffect.asSharedFlow()

    fun onIntent(intent: FeedIntent) {
        when (intent) {
            is FeedIntent.Init -> handleInit()
            is FeedIntent.InfoBtnClick -> handleInfoBtnClick()
            is FeedIntent.TooltipClick -> handleTooltipClick()
            is FeedIntent.ListScroll -> handleListScroll()
        }
    }

    private fun handleInit() {
        getExamplePromptsFromServer()
    }

    private fun handleInfoBtnClick() {

    }

    private fun handleTooltipClick() {
        _feedState.update {
            it.copy(isTooltipVisible = false, isTooltipClosed = true)
        }
    }

    private fun handleListScroll() {
        _feedState.update {
            it.copy(isTooltipVisible = true)
        }
        AmplitudeManager.apply {
            trackEvent("scroll_main_3pic")
            plusIntProperties("user_main_scroll")
        }
    }

    private fun changeLoadingState(isLoading: Boolean) {
        _feedState.update {
            it.copy(isLoading = isLoading)
        }
    }

    private fun getExamplePromptsFromServer() {
        viewModelScope.launch {
            changeLoadingState(true)
//            feedRepository.getExampleItems()
//                .onSuccess { list ->
//                    _feedState.update {
//                        it.copy(itemList = list.toImmutableList())
//                    }
//                }
//                .onFailure {
//                    _feedSideEffect.emit(FeedSideEffect.ShowErrorToast)
//                }
            // TODO: 임시 리스트
            _feedState.update {
                it.copy(
                    itemList = persistentListOf(
                        FeedItemModel(
                            picture = ImageModel(
                                id = 10L,
                                url = "https://d2rvmd5lmgmzuf.cloudfront.net/DEV/ADMIN_UPLOADED_IMAGE/10.png",
                                pictureRatio = PictureRatio.RATIO_GARO
                            ),
                            prompt = "바다가 보이는 절벽 같은 곳 옆에서 캠핑용 텐트를 치고 서있는 모습을 찍어주세용 날씨는 흐리고 저는 자연스럽게 서있게 해주세요~~ 옷은 적당히 맨투맨에 베이지색 바지로 입으면 좋겠습니다"
                        ),
                        FeedItemModel(
                            picture = ImageModel(
                                id = 133L,
                                url = "https://d2rvmd5lmgmzuf.cloudfront.net/DEV/ADMIN_UPLOADED_IMAGE/피드_2.png",
                                pictureRatio = PictureRatio.RATIO_SERO
                            ),
                            prompt = "과수원에서 사과를 따고 있는 아빠 사진"
                        ),
                        FeedItemModel(
                            picture = ImageModel(
                                id = 46L,
                                url = "https://d2rvmd5lmgmzuf.cloudfront.net/DEV/ADMIN_UPLOADED_IMAGE/46.png",
                                pictureRatio = PictureRatio.RATIO_GARO
                            ),
                            prompt = "뉴욕 타임스퀘어 느낌의 여행지를 돌아다니는 느낌으로 만들어 주세요. 전광판들이 되게 화려하게 빛나고 있고, 사람들도 많이 있는 느낌을 내주면 좋겠습니다!"
                        ),
                        FeedItemModel(
                            picture = ImageModel(
                                id = 2L,
                                url = "https://d2rvmd5lmgmzuf.cloudfront.net/DEV/ADMIN_UPLOADED_IMAGE/2.png",
                                pictureRatio = PictureRatio.RATIO_SERO
                            ),
                            prompt = "넓게 펼쳐진 푸르고 에메랄드 색의 바다를 뒤에 두고 의자에 자연스럽게 앉아있는 모습을 찍어주세요. 의자 주변에는 현무암 같은 돌들이 자연스럽게 놓여있어서, 바다 느낌이 더 잘 났으면 좋겠습니다. 저는 검정색 원피스에 어울리는 신발을 신고 있으면 좋을 것 같아요 ㅎㅎ"
                        ),
                        FeedItemModel(
                            picture = ImageModel(
                                id = 23L,
                                url = "https://d2rvmd5lmgmzuf.cloudfront.net/DEV/ADMIN_UPLOADED_IMAGE/23.png",
                                pictureRatio = PictureRatio.RATIO_SERO
                            ),
                            prompt = "뒤 창문으로 나무와 풀들이 보이는 카페에서 커피를 먹고 있고 싶어요. 쫄티와 베이지색 치마를 입고, 흰색 신발을 신고 있으면 좋겠습니당. 테이블에는 커피랑 케이크 같은게 자연스럽게 놓여있으면 좋겠네요!!"
                        )
                    )
                )
            }
            changeLoadingState(false)
        }
    }
}
