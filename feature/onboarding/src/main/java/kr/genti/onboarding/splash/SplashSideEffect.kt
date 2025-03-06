package kr.genti.onboarding.splash

sealed class SplashSideEffect {
    data object ShowErrorToast : SplashSideEffect()
    data object NavigateToLogin : SplashSideEffect()
    data object NavigateToFeed : SplashSideEffect()
    data object StartAppUpdate : SplashSideEffect()
}