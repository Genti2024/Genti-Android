package kr.genti.common.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionManager {
    fun checkPermissionAndLaunch(
        permission: String,
        context: Context,
        onPermissionGranted: () -> Unit,
        onPermissionNotGranted: () -> Unit,
        onPermissionAlreadyDenied: (Intent) -> Unit
    ) {
        if (isPermissionGranted(permission, context)) {
            onPermissionGranted()
        } else {
            if (!isPermissionAlreadyRejected(permission, context)) {
                onPermissionNotGranted()
            } else {
                onPermissionAlreadyDenied(intentToSetting(context))
            }
        }
    }

    fun isPermissionGranted(permission: String, context: Context): Boolean =
        ContextCompat.checkSelfPermission(
            context, permission
        ) == PackageManager.PERMISSION_GRANTED

    private fun isPermissionAlreadyRejected(permission: String, context: Context): Boolean =
        (context as? Activity).run {
            this?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(it, permission)
            } ?: false
        }

    private fun intentToSetting(context: Context) =
        Intent(ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.applicationContext.packageName, null)
        }
}