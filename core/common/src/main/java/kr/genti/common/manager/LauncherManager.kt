package kr.genti.common.manager

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable

object LauncherManager {
    @Composable
    fun rememberPermissionLauncher(
        onPermissionGranted: () -> Unit
    ): ManagedActivityResultLauncher<String, Boolean> {
        return rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) onPermissionGranted()
        }
    }

    @Composable
    fun rememberIntentSenderLauncher(
        onAppUpdateResult: (isAppUpdateSuccess: Boolean) -> Unit
    ): ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult> {
        return rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                onAppUpdateResult(true)
            } else {
                onAppUpdateResult(false)
            }
        }
    }

    @Composable
    fun rememberPhotoPickerLauncher(
        maxItems: Int = 3,
        onImageSelected: (List<Uri>) -> Unit
    ): ManagedActivityResultLauncher<PickVisualMediaRequest, List<Uri>> {
        return rememberLauncherForActivityResult(
            contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems)
        ) { uriList ->
            if (uriList.isNotEmpty()) onImageSelected(uriList)
        }
    }

    @Composable
    fun rememberGalleryPickerLauncher(
        onImageSelected: (List<Uri>) -> Unit
    ): ManagedActivityResultLauncher<Intent, ActivityResult> {
        return rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uriList: List<Uri> = result.data?.clipData?.let { clipData ->
                    (0 until clipData.itemCount).mapNotNull { index -> clipData.getItemAt(index)?.uri }
                } ?: result.data?.data?.let { listOf(it) } ?: emptyList()
                if (uriList.isNotEmpty()) onImageSelected(uriList)
            }
        }
    }

    fun getMultipleGalleryPickerIntent() =
        Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        }
}