package kr.genti.onboarding.tutorial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.genti.onboarding.model.TutorialStage
import javax.inject.Inject


@HiltViewModel
class TutorialViewModel @Inject constructor() : ViewModel() {
    private val _tutorialState = MutableStateFlow(TutorialState())
    val tutorialState = _tutorialState.asStateFlow()

    private val _tutorialSideEffect = MutableSharedFlow<TutorialSideEffect>()
    val tutorialSideEffect = _tutorialSideEffect.asSharedFlow()

    fun onIntent(intent: TutorialIntent) {
        when (intent) {
            is TutorialIntent.NextBtnClick -> handleNextBtnClick()
            is TutorialIntent.CloseBtnClick -> handleCloseBtnClick()
        }
    }

    private fun handleNextBtnClick() {
        when (tutorialState.value.currentStage) {
            TutorialStage.FIRST -> {
                _tutorialState.update { it.copy(currentStage = TutorialStage.SECOND) }
            }

            TutorialStage.SECOND -> {
                _tutorialState.update { it.copy(currentStage = TutorialStage.THIRD) }
            }

            TutorialStage.THIRD -> {
                viewModelScope.launch {
                    _tutorialSideEffect.emit(TutorialSideEffect.NavigateToFeed)
                }
            }
        }
    }

    private fun handleCloseBtnClick() {
        viewModelScope.launch {
            _tutorialSideEffect.emit(TutorialSideEffect.NavigateToFeed)
        }
    }
}