package kr.genti.main

import kr.genti.main.navigation.MainTab

sealed class MainIntent {
    data class TabSelect(val tab: MainTab) : MainIntent()
    data object GenerateBtnClick : MainIntent()
}