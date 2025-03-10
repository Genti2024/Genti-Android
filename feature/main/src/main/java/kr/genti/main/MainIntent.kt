package kr.genti.main

import kr.genti.main.navigation.MainTab

sealed class MainIntent {
    data class TabSelect(val tab: MainTab) : MainIntent()
    data object GenerateBtnClick : MainIntent()
    data class PushAlarmReceived(val type: String?) : MainIntent()
    data object DialogDismiss : MainIntent()
    data object RegenerateDialogBtnClick : MainIntent()
    data object FinishedDialogBtnClick : MainIntent()
    data object SelectDialogBtnClick : MainIntent()
    data object DebugPatchBtnClick : MainIntent()
}