package kr.genti.generate

sealed class GenerateSideEffect {
    data object ShowErrorToast : GenerateSideEffect()
    data object NavigateToWaiting : GenerateSideEffect()
    data object NavigateToBack : GenerateSideEffect()
    data class StartImageSelect(val isExtra: Boolean) : GenerateSideEffect()
}