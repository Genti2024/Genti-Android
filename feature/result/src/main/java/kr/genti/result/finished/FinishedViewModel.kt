package kr.genti.result.finished

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
            is FinishedIntent.Init -> handleInit(intent)
            is FinishedIntent.ImageClick -> handleImageClick()
            is FinishedIntent.BackButtonClick -> handleBackButtonClick()
            is FinishedIntent.ReportButtonClick -> handleReportButtonClick()
            is FinishedIntent.ShareButtonClick -> handleShareButtonClick()
            is FinishedIntent.DownloadButtonClick -> handleDownloadButtonClick()
            is FinishedIntent.ReportDialogButtonClick -> handleReportDialogButtonClick()
            is FinishedIntent.RatingDialogButtonClick -> handleRatingDialogButtonClick()
            is FinishedIntent.DialogDismiss -> handleDialogDismiss()
        }
    }

    private fun handleInit(intent: FinishedIntent.Init) {
        _finishedState.update {
            it.copy(
                responseId = intent.responseId,
                imageUrl = intent.imageUrl,
                isGaro = intent.isGaro,
                isParentPic = intent.isParentPic
            )
        }
    }

    private fun handleImageClick() {

    }

    private fun handleBackButtonClick() {

    }

    private fun handleReportButtonClick() {

    }

    private fun handleShareButtonClick() {

    }

    private fun handleDownloadButtonClick() {

    }

    private fun handleReportDialogButtonClick() {

    }

    private fun handleRatingDialogButtonClick() {

    }

    private fun handleDialogDismiss() {

    }
}