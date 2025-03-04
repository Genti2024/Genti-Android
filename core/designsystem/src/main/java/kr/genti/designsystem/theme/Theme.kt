package kr.genti.designsystem.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val colorScheme = darkColorScheme(
    primary = White,
    background = Black,
)

private val LocalGentiTypography = staticCompositionLocalOf<GentiTypography> {
    error("No Typography provided")
}

object GentiTheme {
    val typography: GentiTypography
        @Composable get() = LocalGentiTypography.current
}

@Composable
fun ProvideGentiTypography(typography: GentiTypography, content: @Composable () -> Unit) {
    val provideTypography = remember { typography.copy() }
    provideTypography.update(typography)
    CompositionLocalProvider(
        LocalGentiTypography provides provideTypography,
        content = content
    )
}

@Composable
fun GentiTheme(
    content: @Composable () -> Unit,
) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = true
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightNavigationBars = true
        }
    }

    val typography = gentiTypography()

    ProvideGentiTypography(typography) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
