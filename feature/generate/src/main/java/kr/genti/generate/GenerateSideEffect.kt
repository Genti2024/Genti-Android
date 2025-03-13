package kr.genti.generate

sealed class GenerateSideEffect {
    data object ShowErrorToast : GenerateSideEffect()
    data class NavigateToWaiting(val isParentPic: Boolean) : GenerateSideEffect()
    data object NavigateToBack : GenerateSideEffect()
    data object StartImageSelect : GenerateSideEffect()
    data object StartPurchaseProduct : GenerateSideEffect()
}