package kr.genti.core.extension

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat

fun Activity.setStatusBarColorFromResource(colorResId: Int) {
    val statusBarColor = ContextCompat.getColor(this, colorResId)
    window.statusBarColor = statusBarColor
}

fun Activity.setNavigationBarColorFromResource(colorResId: Int) {
    val navigationBarColor = ContextCompat.getColor(this, colorResId)
    window.navigationBarColor = navigationBarColor
}

fun Activity.getWindowHeight(): Int {
    val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = wm.currentWindowMetrics
        val insets =
            windowMetrics.windowInsets
                .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars())
        windowMetrics.bounds.height() - insets.bottom - insets.top
    } else {
        val displayMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.heightPixels
    }
}

fun ComponentActivity.initOnBackPressedListener(
    view: View,
    delay: Long = 2000L,
) {
    var backPressedTime = 0L
    val onBackPressedCallback =
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - backPressedTime >= delay) {
                    backPressedTime = System.currentTimeMillis()
                    view.context.toast("버튼을 한번 더 누르면 종료됩니다.")
                } else {
                    finish()
                }
            }
        }

    this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
}
