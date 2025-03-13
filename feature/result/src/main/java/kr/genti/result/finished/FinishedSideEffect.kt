package kr.genti.result.finished

import android.net.Uri

sealed class FinishedSideEffect {
    data object ShowErrorToast : FinishedSideEffect()
    data object ShowDownloadToast : FinishedSideEffect()
    data object NavigateToBack : FinishedSideEffect()
    data object StartPermissionLauncher : FinishedSideEffect()
    data class NavigateToShare(val imageUri: Uri) : FinishedSideEffect()
}