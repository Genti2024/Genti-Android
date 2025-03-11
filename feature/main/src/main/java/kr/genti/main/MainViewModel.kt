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
import timber.log.Timber
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
            is MainIntent.PushAlarmReceived -> handlePushAlarmReceived(intent.type)
            is MainIntent.DialogDismiss -> handleDialogDismiss()
            is MainIntent.RegenerateDialogBtnClick -> handleRegenerateDialogBtnClick()
            is MainIntent.FinishedDialogBtnClick -> handleFinishedDialogBtnClick()
            is MainIntent.SelectDialogBtnClick -> handleSelectDialogBtnClick(intent.isParentPic)
            is MainIntent.DebugPatchBtnClick -> handleDebugPatchBtnClick()
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

    private fun handlePushAlarmReceived(type: String?) {
        Timber.tag("okhttp").d("NEW ALARM RECEIVED (type : $type)")
        if (type == TYPE_SUCCESS || type == TYPE_CANCELED) handleGenerateBtnClick()
    }

    private fun handleDialogDismiss() {
        viewModelScope.launch {
            _mainState.update {
                it.copy(
                    isErrorDialogVisible = false,
                    isUnableDialogVisible = false,
                    isFinishedDialogVisible = false,
                    isSelectDialogVisible = false
                )
            }
            if (mainState.value.currentGenerateStatus == GenerateStatus.CANCELED) {
                postToResetGenerateStatus()
            }
        }
    }

    private fun handleRegenerateDialogBtnClick() {
        handleDialogDismiss()
        viewModelScope.launch {
            getGenerateStatus()
        }
    }

    private fun handleFinishedDialogBtnClick() {
        handleDialogDismiss()
        viewModelScope.launch {
            _mainSideEffect.emit(MainSideEffect.NavigateToFinished)
        }
    }

    private fun handleSelectDialogBtnClick(isParentPic: Boolean) {
        handleDialogDismiss()
        AmplitudeManager.trackEvent(if (isParentPic) "click_parentpicture" else "click_mypicture")
        viewModelScope.launch {
            _mainSideEffect.emit(MainSideEffect.NavigateToGenerate(isParentPic))
        }
    }

    private fun handleDebugPatchBtnClick() {
        viewModelScope.launch {
            patchStatusInDevelop()
        }
    }

    private suspend fun getGenerateStatus() {
        generateRepository.getGenerateStatus()
            .onSuccess { result ->
                _mainState.update {
                    it.copy(
                        generatedImage = result,
                        currentGenerateStatus = result.status
                    )
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
                        it.copy(
                            isUnableDialogVisible = true,
                            serverUnableMessage = result.message.toString()
                        )
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

    companion object {
        const val TYPE_SUCCESS = "SUCCESS"
        const val TYPE_CANCELED = "CANCELED"
    }
}