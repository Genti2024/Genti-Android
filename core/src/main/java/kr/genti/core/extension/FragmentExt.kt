package kr.genti.core.extension

import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar

fun Fragment.toast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(message: String) {
    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
}

fun Fragment.snackBar(
    anchorView: View,
    message: () -> String,
) {
    Snackbar.make(anchorView, message(), Snackbar.LENGTH_SHORT).show()
}

fun Fragment.stringOf(
    @StringRes resId: Int,
) = getString(resId)

fun Fragment.colorOf(
    @ColorRes resId: Int,
) = ContextCompat.getColor(requireContext(), resId)

fun Fragment.drawableOf(
    @DrawableRes resId: Int,
) = ContextCompat.getDrawable(requireContext(), resId)

fun Fragment.setStatusBarColor(colorResId: Int) {
    activity?.let {
        val statusBarColor = ContextCompat.getColor(it, colorResId)
        it.window.statusBarColor = statusBarColor
    }
}

fun Fragment.initOnBackPressedListener(
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
                    requireActivity().finish()
                }
            }
        }

    requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
}

val Fragment.viewLifeCycle
    get() = viewLifecycleOwner.lifecycle

val Fragment.viewLifeCycleScope
    get() = viewLifecycleOwner.lifecycleScope
