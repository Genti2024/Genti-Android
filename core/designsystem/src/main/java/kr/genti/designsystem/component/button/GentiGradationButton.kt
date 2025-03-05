package kr.genti.designsystem.component.button

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiGradationEnd
import kr.genti.designsystem.theme.GentiGradationStart
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.GrayBtn
import kr.genti.designsystem.theme.Transparent

@Composable
fun GentiGradationButton(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int = R.string.app_name,
    onClick: () -> Unit = {}
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val widthPx = with(density) { maxWidth.toPx() }
        val heightPx = with(density) { maxHeight.toPx() }

        val gradationBrush = Brush.linearGradient(
            colors = listOf(GentiGradationStart, GentiGradationEnd),
            start = Offset(0f, 0f),
            end = Offset(widthPx, heightPx)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClickable { onClick() },
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Transparent),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(brush = gradationBrush)
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(textRes),
                    style = GentiTheme.typography.subtitle2,
                    color = GrayBtn
                )
            }
        }
    }
}

@Preview
@Composable
fun GentiGradationButtonPreview() {
    GentiTheme {
        GentiGradationButton(
            textRes = R.string.btn_download,
        )
    }
}