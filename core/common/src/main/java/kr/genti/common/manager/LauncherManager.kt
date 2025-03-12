package kr.genti.common.manager

import android.app.Activity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
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
}