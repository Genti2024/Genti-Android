package kr.genti.result.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.genti.domain.repository.CreateRepository
import kr.genti.domain.repository.UploadRepository
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel
@Inject
constructor(
    private val createRepository: CreateRepository,
    private val uploadRepository: UploadRepository,
) : ViewModel() {

    private val _verifyState = MutableStateFlow(VerifyState())
    val verifyState = _verifyState.asStateFlow()

    private val _verifySideEffect = MutableSharedFlow<VerifySideEffect>()
    val verifySideEffect = _verifySideEffect.asSharedFlow()

    fun onIntent(intent: VerifyIntent) {
        when (intent) {
            is VerifyIntent.VerifyButtonClick -> handleVerifyButtonClick()
            is VerifyIntent.RetakeButtonClick -> handleRetakeButtonClick()
            is VerifyIntent.FinishButtonClick -> handleFinishButtonClick()
            is VerifyIntent.BackButtonClick -> handleExitDialog(true)
            is VerifyIntent.ExitButtonClick -> handleExitButtonClick()
            is VerifyIntent.ExitDialogDismiss -> handleExitDialog(false)
        }
    }

    private fun handleVerifyButtonClick() {

    }

    private fun handleRetakeButtonClick() {

    }

    private fun handleFinishButtonClick() {

    }

    private fun handleExitButtonClick() {
        viewModelScope.launch {
            _verifySideEffect.emit(VerifySideEffect.NavigateToBack)
        }
    }

    private fun handleExitDialog(isVisible: Boolean) {
        _verifyState.update {
            it.copy(isExitDialogVisible = isVisible)
        }
    }
}