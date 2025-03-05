package kr.genti.designsystem.component.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiTheme

@Composable
fun GentiTopBottomShadow(
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    val topAlpha by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex == 0) {
                (listState.firstVisibleItemScrollOffset / 200f).coerceIn(0f, 1f)
            } else {
                1f
            }
        }
    }

    var viewportHeight by remember { mutableIntStateOf(0) }

    val bottomAlpha by remember {
        derivedStateOf {
            val totalItems = listState.layoutInfo.totalItemsCount
            val lastItemInfo = listState.layoutInfo.visibleItemsInfo.lastOrNull()
            if (totalItems > 0 && lastItemInfo != null && lastItemInfo.index == totalItems - 1) {
                (((lastItemInfo.offset + lastItemInfo.size) - viewportHeight) / 200f)
                    .coerceIn(0f, 1f)
            } else {
                1f
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .onSizeChanged { viewportHeight = it.height }
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_gradation_black_top),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .alpha(topAlpha)
        )
        Image(
            painter = painterResource(id = R.drawable.img_gradation_black_bottom),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .alpha(bottomAlpha)
        )
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
private fun GentiTopBottomShadowPreview() {
    GentiTheme {
        GentiTopBottomShadow(listState = LazyListState())
    }
}