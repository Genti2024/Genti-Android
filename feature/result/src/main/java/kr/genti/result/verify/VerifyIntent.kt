package kr.genti.result.verify

sealed class VerifyIntent {
    data class CameraButtonClick(val isFirst: Boolean) : VerifyIntent()
    data object CameraPermissionGrant : VerifyIntent()
    data object CameraResultSuccess : VerifyIntent()
    data object FinishButtonClick : VerifyIntent()
    data object BackButtonClick : VerifyIntent()
    data object ExitButtonClick : VerifyIntent()
    data object ExitDialogDismiss : VerifyIntent()
}