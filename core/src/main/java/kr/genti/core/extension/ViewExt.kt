package kr.genti.core.extension

import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.view.View

inline fun View.setOnSingleClickListener(
    delay: Long = 1000L,
    crossinline block: (View) -> Unit,
) {
    var previousClickedTime = 0L
    setOnClickListener { view ->
        val clickedTime = System.currentTimeMillis()
        if (clickedTime - previousClickedTime >= delay) {
            block(view)
            previousClickedTime = clickedTime
        }
    }
}

fun View.setGusianBlur(radius: Float?) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (radius != null) {
            val blurEffect =
                RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.REPEAT)
            this.setRenderEffect(blurEffect)
        } else {
            this.setRenderEffect(null)
        }
    }
}
