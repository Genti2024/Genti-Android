package kr.genti.generate.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.White20

@Composable
internal fun GenerateProgressBar(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    LinearProgressIndicator(
        progress = { animatedProgress },
        modifier = modifier.fillMaxWidth(),
        color = GentiGreen,
        trackColor = White20,
        gapSize = 0.dp,
        drawStopIndicator = { }
    )
}

@Preview
@Composable
private fun GenerateProgressBarPreview() {
    GentiTheme {
        GenerateProgressBar(
            progress = 0.33F
        )
    }
}