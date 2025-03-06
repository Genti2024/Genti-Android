package kr.genti.onboarding.splash

data class SplashState(
    val isSignedBefore: Boolean = false,
    val isLottieFinished: Boolean = false,
    val isCheckFinished: Boolean = false
)