package kr.genti.result.verify

sealed class VerifySideEffect {
    data object ShowErrorToast : VerifySideEffect()
    data object NavigateToBack : VerifySideEffect()
}