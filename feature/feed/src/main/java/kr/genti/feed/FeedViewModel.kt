package kr.genti.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
        }
    }

    private fun handleInit() {
        getExamplePromptsFromServer()
    }

    private fun handleInfoBtnClick() {

    }

    private fun handleTooltipClick() {
        _feedState.update {
            it.copy(isTooltipVisible = false)
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
            feedRepository.getExampleItems()
                .onSuccess { list ->
                    _feedState.update {
                        it.copy(itemList = list.toImmutableList())
                    }
                }
                .onFailure {
                    _feedSideEffect.emit(FeedSideEffect.ShowErrorToast)
                }
            changeLoadingState(false)
        }
    }
}
