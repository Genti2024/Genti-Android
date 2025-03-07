package kr.genti.onboarding.signup

sealed class SignupSideEffect {
    data object ShowErrorToast : SignupSideEffect()
    data object NavigateToTutorial : SignupSideEffect()
}