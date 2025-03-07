package kr.genti.onboarding.login

data class LoginState(
    val isAppLoginAvailable: Boolean = false,
    val isLoading: Boolean = false,
)