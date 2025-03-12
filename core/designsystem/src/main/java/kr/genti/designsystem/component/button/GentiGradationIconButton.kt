package kr.genti.designsystem.component.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.Disabled
import kr.genti.designsystem.theme.GentiGradationEnd
import kr.genti.designsystem.theme.GentiGradationStart

@Composable
fun GentiGradationIconButton(
    @DrawableRes iconRes: Int,
    modifier: Modifier = Modifier,
    isActivate: Boolean = false,
    onBtnClick: () -> Unit = {}
) {
    val gradationBrush = Brush.linearGradient(
        colors = listOf(GentiGradationStart, GentiGradationEnd),
        start = Offset(0f, 0f),
        end = Offset(80f, 80f)
    )
    Box(modifier = modifier) {
        Icon(
            imageVector = ImageVector.vectorResource(iconRes),
            contentDescription = null,
            tint = Black,
            modifier = modifier
                .then(
                    if (isActivate) {
                        Modifier.background(
                            brush = gradationBrush,
                            shape = RoundedCornerShape(8.dp)
                        )
                    } else {
                        Modifier.background(
                            color = Disabled,
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                )
                .padding(10.dp)
                .noRippleClickable { if (isActivate) onBtnClick() }
        )
    }
}

@Preview
@Composable
private fun GentiGradationIconButtonPreview() {
    Column {
        GentiGradationIconButton(
            iconRes = R.drawable.ic_play,
            isActivate = true
        )
        Spacer(modifier = Modifier.height(16.dp))
        GentiGradationIconButton(
            iconRes = R.drawable.ic_play,
            isActivate = false
        )
    }
}