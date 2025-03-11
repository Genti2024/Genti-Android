package kr.genti.result.verify

data class VerifyState(
    val isPhotoTaken: Boolean = false,
    val isLoading: Boolean = false,
    val isExitDialogVisible: Boolean = false,
)