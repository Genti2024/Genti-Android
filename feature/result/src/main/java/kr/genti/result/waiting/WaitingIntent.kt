package kr.genti.result.waiting

sealed class WaitingIntent {
    data class Init(val isParentPic: Boolean) : WaitingIntent()
    data object ReturnButtonClick : WaitingIntent()
    data object AlarmDialogRequestButtonClick : WaitingIntent()
    data object AlarmDialogReturnButtonClick : WaitingIntent()
    data object AlarmRequestGrant: WaitingIntent()
    data object AlarmDialogDismiss : WaitingIntent()
}