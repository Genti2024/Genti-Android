package kr.genti.result.finished

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.genti.domain.entity.request.ReportRequestModel
import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

@HiltViewModel
class FinishedViewModel
@Inject
constructor(
    private val generateRepository: GenerateRepository
) : ViewModel() {
    private val _finishedState = MutableStateFlow(FinishedState())
    val finishedState = _finishedState.asStateFlow()

    private val _finishedSideEffect = MutableSharedFlow<FinishedSideEffect>()
    val finishedSideEffect = _finishedSideEffect.asSharedFlow()

    fun onIntent(intent: FinishedIntent) {
        when (intent) {
            is FinishedIntent.Init -> handleInit(intent.responseId)
            is FinishedIntent.ImageClick -> handleImageClick()
            is FinishedIntent.BackButtonClick -> handleBackButtonClick()
            is FinishedIntent.ReportButtonClick -> handleReportButtonClick()
            is FinishedIntent.DialogDismiss -> handleDialogDismiss()
            is FinishedIntent.ShareButtonClick -> handleShareButtonClick()
            is FinishedIntent.DownloadButtonClick -> handleDownloadButtonClick()
            is FinishedIntent.ReportTextChange -> handleReportTextChange(intent.text)
            is FinishedIntent.ReportSubmitButtonClick -> handleReportSubmitButtonClick()
            is FinishedIntent.FinishButtonClick -> handleFinishButtonClick()
            is FinishedIntent.RatingSubmitButtonClick -> handleRatingSubmitButtonClick()
            is FinishedIntent.RatingSkipButtonClick -> handleRatingSkipButtonClick()
        }
    }

    private fun handleInit(responseId: Long) {
        _finishedState.update {
            it.copy(responseId = responseId)
        }
    }

    private fun handleImageClick() {
        _finishedState.update {
            it.copy(isDetailDialogVisible = true)
        }
    }

    private fun handleBackButtonClick() {
        _finishedState.update {
            it.copy(isRatingDialogVisible = true)
        }
    }

    private fun handleReportButtonClick() {
        _finishedState.update {
            it.copy(isReportDialogVisible = true)
        }
    }

    private fun handleDialogDismiss() {
        _finishedState.update {
            it.copy(
                isDetailDialogVisible = false,
                isReportDialogVisible = false,
                isRatingDialogVisible = false
            )
        }
    }

    private fun handleShareButtonClick() {

    }

    private fun handleDownloadButtonClick() {

    }

    private fun handleReportTextChange(text: String) {
        _finishedState.update {
            it.copy(reportText = text)
        }
    }

    private fun handleReportSubmitButtonClick() {
        viewModelScope.launch {
            generateRepository.postGenerateReport(
                ReportRequestModel(
                    finishedState.value.responseId,
                    finishedState.value.reportText,
                ),
            ).onSuccess {
                _finishedState.update {
                    it.copy(isReportSubmitted = true)
                }
            }.onFailure {
                _finishedSideEffect.emit(FinishedSideEffect.ShowErrorToast)
            }
        }
    }

    private fun handleFinishButtonClick() {
        viewModelScope.launch {
            _finishedSideEffect.emit(FinishedSideEffect.NavigateToBack)
        }
    }

    private fun handleRatingSubmitButtonClick() {

    }

    private fun handleRatingSkipButtonClick() {

    }
}