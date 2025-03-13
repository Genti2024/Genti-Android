package kr.genti.result.finished

data class FinishedState(
    val responseId: Long = -1,
    val isDetailDialogVisible: Boolean = false,
    val isReportDialogVisible: Boolean = false,
    val isRatingDialogVisible: Boolean = false,
)