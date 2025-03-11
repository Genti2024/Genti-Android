package kr.genti.result.verify

sealed class VerifySideEffect {
    data object ShowErrorToast : VerifySideEffect()
    data object NavigateToBack : VerifySideEffect()
    data object StartPermissionLauncher : VerifySideEffect()
    data object StartCameraLauncher : VerifySideEffect()
    data object VerifySuccess: VerifySideEffect()
}