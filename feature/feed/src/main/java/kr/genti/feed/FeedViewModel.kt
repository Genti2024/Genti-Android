package kr.genti.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.genti.common.manager.AmplitudeManager
import kr.genti.domain.usecase.feed.GetFeedItemListUseCase
import javax.inject.Inject

@HiltViewModel
class FeedViewModel
@Inject
constructor(
    private val getFeedItemListUseCase: GetFeedItemListUseCase
) : ViewModel() {
    private val _feedState = MutableStateFlow(FeedState())
    val feedState = _feedState.asStateFlow()

    private val _feedSideEffect = MutableSharedFlow<FeedSideEffect>()
    val feedSideEffect = _feedSideEffect.asSharedFlow()

    fun onIntent(intent: FeedIntent) {
        when (intent) {
            is FeedIntent.Init -> handleInit()
            is FeedIntent.Refresh -> handleRefresh()
            is FeedIntent.InfoBtnClick -> handleInfoBtnClick()
            is FeedIntent.TooltipClick -> handleTooltipClick()
            is FeedIntent.ListScroll -> handleListScroll()
            is FeedIntent.BottomSheetDismiss -> handleBottomSheetDismiss()
            is FeedIntent.MoreBtnClick -> handleMoreBtnClick()
        }
    }

    private fun handleInit() {
        viewModelScope.launch {
            changeLoadingState(true)
            getExamplePromptsFromServer()
            changeLoadingState(false)
            _feedState.update {
                it.copy(isRefreshing = false)
            }
        }
    }

    private fun handleRefresh() {
        viewModelScope.launch {
            _feedState.update { it.copy(isRefreshing = true) }
            getExamplePromptsFromServer()
            delay(1_000)
            _feedState.update { it.copy(isRefreshing = false) }
        }
    }

    private fun handleInfoBtnClick() {
        _feedState.update {
            it.copy(isBottomSheetVisible = true)
        }
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

    private fun handleBottomSheetDismiss() {
        _feedState.update {
            it.copy(isBottomSheetVisible = false)
        }
    }

    private fun handleMoreBtnClick() {
        _feedState.update {
            it.copy(isBottomSheetVisible = false)
        }
        viewModelScope.launch {
            _feedSideEffect.emit(FeedSideEffect.NavigateToWebsite(WEB_GENFLUENCER))
        }
    }

    private fun changeLoadingState(isLoading: Boolean) {
        _feedState.update {
            it.copy(isLoading = isLoading)
        }
    }

    private suspend fun getExamplePromptsFromServer() {
        getFeedItemListUseCase()
            .onSuccess { list ->
                _feedState.update {
                    it.copy(itemList = list.toImmutableList())
                }
            }.onFailure {
                _feedSideEffect.emit(FeedSideEffect.ShowErrorToast)
            }
    }

    companion object {
        private const val WEB_GENFLUENCER =
            "https://stealth-goose-156.notion.site/57a00e1d610b4c1786c6ab1fdb4c4659?pvs=4"
    }
}
