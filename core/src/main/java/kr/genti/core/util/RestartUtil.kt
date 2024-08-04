package kr.genti.core.util

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent

object RestartUtil {
    fun restartApp(
        context: Context,
        toastMsg: String?,
    ) {
        val packageManager = context.packageManager
        val packageName = context.packageName
        val componentName = packageManager.getLaunchIntentForPackage(packageName)?.component
        Intent.makeRestartActivityTask(componentName).apply {
            if (toastMsg != null) putExtra("TOAST_MSG", toastMsg)
            context.startActivity(
                this,
                ActivityOptions.makeCustomAnimation(
                    context,
                    0,
                    0,
                ).toBundle(),
            )
        }
        Runtime.getRuntime().exit(0)
    }
}
