package kr.genti.result.finished

data class FinishedState(
    val responseId: Long = -1,
    val imageUrl: String = "",
    val isGaro: Boolean = false,
    val isParentPic: Boolean = false,
    val isDetailDialogVisible: Boolean = false,
    val isReportDialogVisible: Boolean = false,
    val isRatingDialogVisible: Boolean = false,
)