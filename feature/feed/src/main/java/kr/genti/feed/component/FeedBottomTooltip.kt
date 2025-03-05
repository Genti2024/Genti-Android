package kr.genti.feed.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R

@Composable
internal fun FeedBottomTooltip(
    modifier: Modifier = Modifier,
    isTooltipVisible: Boolean = false,
    onTooltipClick: () -> Unit = {}
) {
    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = isTooltipVisible,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Image(
                painter = painterResource(R.drawable.img_tooltip_feed),
                contentDescription = null,
                modifier = Modifier
                    .width(174.dp)
                    .padding(bottom = 16.dp)
                    .noRippleClickable { onTooltipClick() }
            )
        }
    }
}

@Preview
@Composable
private fun FeedBottomTooltipPreview() {
    FeedBottomTooltip(isTooltipVisible = true)
}