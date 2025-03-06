package kr.genti.onboarding.login

sealed class LoginSideEffect {
    data object ShowErrorToast : LoginSideEffect()
    data object NavigateToSignup : LoginSideEffect()
    data object NavigateToFeed : LoginSideEffect()
    data object StartKakaoLogin : LoginSideEffect()
}