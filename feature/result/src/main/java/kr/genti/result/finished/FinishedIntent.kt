package kr.genti.result.finished

sealed class FinishedIntent {
    data class Init(val responseId: Long) : FinishedIntent()
    data object ImageClick : FinishedIntent()
    data object BackButtonClick : FinishedIntent()
    data object ReportButtonClick : FinishedIntent()
    data object ShareButtonClick : FinishedIntent()
    data object DownloadButtonClick : FinishedIntent()
    data object DialogDismiss : FinishedIntent()
    data class ReportTextChange(val text: String) : FinishedIntent()
    data object ReportSubmitButtonClick : FinishedIntent()
    data object FinishButtonClick : FinishedIntent()
    data object RatingSubmitButtonClick : FinishedIntent()
    data object RatingSkipButtonClick : FinishedIntent()
}