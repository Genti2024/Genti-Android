package kr.genti.designsystem.event

import androidx.compose.runtime.staticCompositionLocalOf

val LocalSnackBarTrigger = staticCompositionLocalOf<(Int) -> Unit> {
    error("LocalSnackBarTrigger not provided")
}