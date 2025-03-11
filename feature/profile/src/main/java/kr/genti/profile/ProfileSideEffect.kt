package kr.genti.profile

import android.net.Uri

sealed class ProfileSideEffect {
    data object ShowErrorToast : ProfileSideEffect()
    data object ShowDownloadToast : ProfileSideEffect()
    data object NavigateToGenerate : ProfileSideEffect()
    data object NavigateToSetting : ProfileSideEffect()
    data object StartPermissionLauncher : ProfileSideEffect()
    data class NavigateToShare(val imageUri: Uri) : ProfileSideEffect()
}