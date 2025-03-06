package kr.genti.onboarding.login

sealed class LoginIntent {
    data object Init : LoginIntent()
    data object LoginBtnClick : LoginIntent()
}