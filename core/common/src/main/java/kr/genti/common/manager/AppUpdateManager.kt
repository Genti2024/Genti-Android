package kr.genti.common.manager

import android.content.Context
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.UpdateAvailability.UPDATE_AVAILABLE
import kotlinx.coroutines.suspendCancellableCoroutine
import kr.genti.core.common.BuildConfig
import kotlin.coroutines.resume

object AppUpdateManager {
    private lateinit var appUpdateManager: AppUpdateManager
    private lateinit var appUpdateInfo: AppUpdateInfo

    fun init(context: Context) {
        appUpdateManager = AppUpdateManagerFactory.create(context)
    }

    suspend fun isAppUpdateAvailable(): Boolean {
        if (BuildConfig.DEBUG) return false
        if (!::appUpdateManager.isInitialized) return false
        return suspendCancellableCoroutine { continuation ->
            appUpdateManager.appUpdateInfo
                .addOnSuccessListener { result ->
                    appUpdateInfo = result
                    continuation.resume(isAppUpdateNeeded())
                }
                .addOnFailureListener {
                    continuation.resume(false)
                }
        }
    }

    private fun isAppUpdateNeeded() =
        appUpdateInfo.updateAvailability() == UPDATE_AVAILABLE &&
                appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE)

    fun startAppUpdate(activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>) {
        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            activityResultLauncher,
            AppUpdateOptions.newBuilder(IMMEDIATE).build()
        )
    }
}