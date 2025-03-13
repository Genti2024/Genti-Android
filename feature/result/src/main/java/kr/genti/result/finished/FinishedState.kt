package kr.genti.result.finished

data class FinishedState(
    val responseId: Long = -1,
    val isParentPic: Boolean = false,
    val reportText: String = "",
    val rating: Int = 5,
    val isReportSubmitted: Boolean = false,
    val isDetailDialogVisible: Boolean = false,
    val isReportDialogVisible: Boolean = false,
    val isRatingDialogVisible: Boolean = false,
)