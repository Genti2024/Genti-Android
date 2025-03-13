package kr.genti.result.waiting

sealed class WaitingSideEffect {
    data object NavigateToBack : WaitingSideEffect()
    data object StartPermissionLauncher : WaitingSideEffect()
}