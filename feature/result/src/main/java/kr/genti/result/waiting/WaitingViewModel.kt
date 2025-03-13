package kr.genti.result.waiting

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
import kr.genti.common.manager.AmplitudeManager.EVENT_CLICK_BTN
import kr.genti.common.manager.AmplitudeManager.PROPERTY_BTN
import kr.genti.common.manager.AmplitudeManager.PROPERTY_PAGE
import javax.inject.Inject

@HiltViewModel
class WaitingViewModel
@Inject
constructor() : ViewModel() {
    private val _waitingState = MutableStateFlow(WaitingState())
    val waitingState = _waitingState.asStateFlow()

    private val _waitingSideEffect = MutableSharedFlow<WaitingSideEffect>()
    val waitingSideEffect = _waitingSideEffect.asSharedFlow()

    fun onIntent(intent: WaitingIntent) {
        when (intent) {
            is WaitingIntent.Init -> handleInit(intent.isParentPic)
            is WaitingIntent.ReturnButtonClick -> handleReturnButtonClick()
            is WaitingIntent.AlarmPermissionNeeded -> handleAlarmPermissionNeeded(intent.isNeeded)
            is WaitingIntent.NavigatedToSetting -> handleNavigateToSetting(true)
            is WaitingIntent.AlarmDialogRequestButtonClick -> handleAlarmRequestButtonClick()
            is WaitingIntent.AlarmDialogReturnButtonClick -> handleAlarmDialogReturnButtonClick()
            is WaitingIntent.AlarmRequestGrant -> handleAlarmRequestGrant()
            is WaitingIntent.AlarmDialogDismiss -> handleAlarmDialogDismiss()
        }
    }

    private fun handleInit(isParentPic: Boolean) {
        _waitingState.update {
            it.copy(isParentPic = isParentPic)
        }
    }

    private fun handleReturnButtonClick() {
        amplitudeTrackBtn(false)
        viewModelScope.launch {
            _waitingSideEffect.emit(WaitingSideEffect.CheckPermission)
        }
    }

    private fun handleAlarmPermissionNeeded(isPermissionNeeded: Boolean) {
        viewModelScope.launch {
            if (isPermissionNeeded) {
                _waitingState.update {
                    it.copy(isAlarmDialogVisible = true)
                }
            } else {
                _waitingSideEffect.emit(WaitingSideEffect.NavigateToBack)
            }
        }
    }

    private fun handleNavigateToSetting(isNavigatedToSetting: Boolean) {
        _waitingState.update {
            it.copy(isNavigatedToSetting = isNavigatedToSetting)
        }
    }

    private fun handleAlarmRequestButtonClick() {
        amplitudeTrackBtn(true)
        viewModelScope.launch {
            _waitingSideEffect.emit(WaitingSideEffect.StartPermissionLauncher)
        }
    }

    private fun handleAlarmDialogReturnButtonClick() {
        viewModelScope.launch {
            handleAlarmDialogDismiss()
            _waitingSideEffect.emit(WaitingSideEffect.NavigateToBack)
        }
    }

    private fun handleAlarmRequestGrant() {
        AmplitudeManager.updateBooleanProperties("user_alarm", true)
        viewModelScope.launch {
            handleAlarmDialogDismiss()
            _waitingSideEffect.emit(WaitingSideEffect.GrantPermission)
        }
    }

    private fun handleAlarmDialogDismiss() {
        _waitingState.update {
            it.copy(isAlarmDialogVisible = false)
        }
    }

    private fun amplitudeTrackBtn(isRequest: Boolean) {
        AmplitudeManager.trackEvent(
            EVENT_CLICK_BTN,
            mapOf(PROPERTY_PAGE to if (isRequest) "alarmagree" else "picwaiting"),
            mapOf(PROPERTY_BTN to if (isRequest) "goalarm" else "gomain"),
        )
    }
}