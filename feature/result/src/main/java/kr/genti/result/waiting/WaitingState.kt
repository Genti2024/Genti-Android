package kr.genti.result.waiting

data class WaitingState(
    val isParentPic: Boolean = false,
    val isAlarmDialogVisible: Boolean = false,
    val isNavigatedToSetting: Boolean = false,
)