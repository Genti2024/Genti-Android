package kr.genti.profile

sealed class ProfileSideEffect {
    data object ShowErrorToast : ProfileSideEffect()
    data object NavigateToGenerate : ProfileSideEffect()
    data object NavigateToSetting : ProfileSideEffect()
}