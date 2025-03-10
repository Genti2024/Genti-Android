package kr.genti.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.genti.common.manager.AmplitudeManager
import kr.genti.domain.enums.GenerateStatus
import kr.genti.domain.repository.GenerateRepository
import kr.genti.main.navigation.MainTab
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val generateRepository: GenerateRepository,
) : ViewModel() {
    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    private val _mainSideEffect = MutableSharedFlow<MainSideEffect>()
    val mainSideEffect = _mainSideEffect.asSharedFlow()

    fun onIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.TabSelect -> handleTabClick(intent.tab)
            is MainIntent.GenerateBtnClick -> handleGenerateBtnClick()
        }
    }

    private fun handleTabClick(tab: MainTab) {
        viewModelScope.launch {
            _mainSideEffect.emit(MainSideEffect.NavigateToTab(tab))
        }
    }

    private fun handleGenerateBtnClick() {
        viewModelScope.launch {
            getGenerateStatus()
        }
    }

    private suspend fun getGenerateStatus() {
        generateRepository.getGenerateStatus()
            .onSuccess { result ->
                _mainState.update {
                    it.copy(generatedImage = result)
                }
                showDialogWithStatus(result.status)
            }.onFailure {
                _mainSideEffect.emit(MainSideEffect.ShowErrorToast)
            }
    }

    private suspend fun showDialogWithStatus(status: GenerateStatus) {
        when (status) {
            GenerateStatus.NEW_REQUEST_AVAILABLE -> {
                getIsServerAvailable()
            }

            GenerateStatus.AWAIT_USER_VERIFICATION -> {
                _mainState.update {
                    it.copy(isFinishedDialogVisible = true)
                }
            }

            GenerateStatus.IN_PROGRESS -> {
                _mainSideEffect.emit(MainSideEffect.NavigateToWaiting)
            }

            GenerateStatus.CANCELED -> {
                _mainState.update {
                    it.copy(isErrorDialogVisible = true)
                }
                postToResetGenerateStatus()
            }

            GenerateStatus.EMPTY -> return
        }
    }

    private suspend fun getIsServerAvailable() {
        generateRepository.getIsServerAvailable()
            .onSuccess { result ->
                if (result.status) {
                    getIsUserVerified()
                } else {
                    _mainState.update {
                        it.copy(isUnableDialogVisible = true)
                    }
                }
            }.onFailure {
                _mainSideEffect.emit(MainSideEffect.ShowErrorToast)
            }
    }

    private suspend fun getIsUserVerified() {
        generateRepository.getIsUserVerified()
            .onSuccess { isVerified ->
                if (isVerified) {
                    AmplitudeManager.trackEvent("click_createpictab")
                    _mainState.update {
                        it.copy(isSelectDialogVisible = true)
                    }
                } else {
                    _mainSideEffect.emit(MainSideEffect.NavigateToVerify)
                }
            }.onFailure {
                _mainSideEffect.emit(MainSideEffect.ShowErrorToast)
            }
    }

    private suspend fun postToResetGenerateStatus() {
        generateRepository.getCanceledToReset(
            mainState.value.generatedImage.requestId.toString()
        ).onFailure {
            _mainSideEffect.emit(MainSideEffect.ShowErrorToast)
        }
    }

    private suspend fun patchStatusInDevelop() {
        generateRepository.patchStatusInDevelop()
            .onSuccess {
                _mainSideEffect.emit(MainSideEffect.ShowStatusChangedToast)
            }.onFailure {
                _mainSideEffect.emit(MainSideEffect.ShowErrorToast)
            }
    }
}