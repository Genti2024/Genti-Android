package kr.genti.result.verify

import android.net.Uri
import kr.genti.domain.entity.response.ImageFileModel

data class VerifyState(
    val isPhotoTaken: Boolean = false,
    val imageUri: Uri? = null,
    val imageName: String? = null,
    val isLoading: Boolean = false,
    val isExitDialogVisible: Boolean = false,
)