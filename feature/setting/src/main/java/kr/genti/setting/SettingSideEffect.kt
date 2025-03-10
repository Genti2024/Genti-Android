package kr.genti.setting

sealed class SettingSideEffect {
    data object ShowErrorToast : SettingSideEffect()
    data class NavigateToWeb(val url: String) : SettingSideEffect()
    data object RestartApp : SettingSideEffect()
}