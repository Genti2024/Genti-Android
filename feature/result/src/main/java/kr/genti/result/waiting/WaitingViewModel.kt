package kr.genti.result.waiting

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
            is WaitingIntent.ReturnButtonClick -> handleBackButtonClick()
            is WaitingIntent.AlarmDialogRequestButtonClick -> handleAlarmButtonClick()
            is WaitingIntent.AlarmDialogDismiss -> handleAlarmDialogDismiss()
        }
    }

    private fun handleInit(isParentPic: Boolean) {

    }

    private fun handleBackButtonClick() {

    }

    private fun handleAlarmButtonClick() {

    }

    private fun handleAlarmDialogDismiss() {

    }
}