package kr.genti.onboarding.tutorial

sealed class TutorialIntent {
    data object NextBtnClick : TutorialIntent()
    data object CloseBtnClick : TutorialIntent()
}