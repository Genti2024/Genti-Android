package kr.genti.onboarding.splash

sealed class SplashIntent {
    data object Init : SplashIntent()
    data object LottiePlayFinish : SplashIntent()
    data class AppUpdateFinish(val isAppUpdateSuccess: Boolean) : SplashIntent()
}