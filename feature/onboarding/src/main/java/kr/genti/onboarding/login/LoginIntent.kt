package kr.genti.onboarding.login

sealed class LoginIntent {
    data class Init(val isAppLoginAvailable: Boolean) : LoginIntent()
    data object LoginBtnClick : LoginIntent()
}