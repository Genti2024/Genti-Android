package kr.genti.main

import kr.genti.main.navigation.MainTab

sealed class MainSideEffect {
    data object ShowErrorToast : MainSideEffect()
    data object ShowStatusChangedToast : MainSideEffect()
    data class NavigateToTab(val tab: MainTab) : MainSideEffect()
    data object NavigateToGenerate : MainSideEffect()
    data object NavigateToVerify : MainSideEffect()
    data object NavigateToWaiting : MainSideEffect()
}