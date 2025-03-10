package kr.genti.onboarding.tutorial

sealed class TutorialSideEffect {
    data object NavigateToFeed : TutorialSideEffect()
}