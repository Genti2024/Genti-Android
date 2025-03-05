package kr.genti.onboarding.splash

sealed class SplashIntent {
    data object Init : SplashIntent()
}