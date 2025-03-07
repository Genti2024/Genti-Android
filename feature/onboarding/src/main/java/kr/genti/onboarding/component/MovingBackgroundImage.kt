package kr.genti.onboarding.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiTheme
import kotlin.math.roundToInt

@Composable
fun MovingBackgroundImage(
    @DrawableRes imageRes: Int,
    modifier: Modifier = Modifier,
) {
    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val density = LocalDensity.current

        // 이미지 Painter 생성 및 크기 측정
        val painter = painterResource(id = imageRes)
        val imageSizePx = painter.intrinsicSize

        // 화면 크기를 픽셀 단위로 변환
        val parentHeightPx = constraints.maxHeight.toFloat()
        val parentWidthPx = constraints.maxWidth.toFloat()

        // 화면 높이에 맞춘 이미지의 가로 길이 측정
        val imageWidthPx = parentHeightPx * (imageSizePx.width / imageSizePx.height)
        val imageWidthDp = with(density) { imageWidthPx.toDp() }
        val imageHeightDp = with(density) { constraints.maxHeight.toDp() }

        // 이미지의 확장된 너비와 화면 너비의 차이 (애니메이션 이동값)
        val imageWidthGapPx = (imageWidthPx - parentWidthPx).coerceAtLeast(0f)

        // 좌우로 -gap/2 ~ +gap/2 범위 내에서 움직이는 애니메이션
        val infiniteTransition = rememberInfiniteTransition()
        val animatedOffset by infiniteTransition.animateFloat(
            initialValue = imageWidthGapPx / 2,
            targetValue = -imageWidthGapPx / 2,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 12000,
                    easing = CubicBezierEasing(0.42f, 0f, 0.58f, 1f)
                ),
                repeatMode = RepeatMode.Reverse
            )
        )

        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .requiredHeight(imageHeightDp)
                .requiredWidth(imageWidthDp)
                .offset { IntOffset(x = animatedOffset.roundToInt(), y = 0) }
        )
    }
}

@Preview
@Composable
private fun MovingBackgroundImagePreview() {
    GentiTheme {
        MovingBackgroundImage(imageRes = R.drawable.img_login_bg)
    }
}