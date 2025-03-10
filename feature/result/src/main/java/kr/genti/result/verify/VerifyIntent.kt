package kr.genti.result.verify

sealed class VerifyIntent {
    data object VerifyButtonClick : VerifyIntent()
    data object RetakeButtonClick : VerifyIntent()
    data object FinishButtonClick : VerifyIntent()
    data object BackButtonClick : VerifyIntent()
    data object ExitButtonClick : VerifyIntent()
    data object ExitDialogDismiss : VerifyIntent()
}