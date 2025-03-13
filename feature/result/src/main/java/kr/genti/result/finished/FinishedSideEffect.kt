package kr.genti.result.finished

sealed class FinishedSideEffect {
    data object ShowErrorToast : FinishedSideEffect()
    data object NavigateToBack : FinishedSideEffect()
}