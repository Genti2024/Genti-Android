package kr.genti.profile

sealed class ProfileSideEffect {
    data object ShowErrorToast : ProfileSideEffect()
    data object ShowDownloadToast : ProfileSideEffect()
    data object NavigateToGenerate : ProfileSideEffect()
    data object NavigateToSetting : ProfileSideEffect()
    data object RequestPermission : ProfileSideEffect()
}