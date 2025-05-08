package kr.genti.designsystem.component.layout

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults.Indicator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kr.genti.designsystem.theme.Gray
import kr.genti.designsystem.theme.White80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GentiPullToRefreshBox(
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit = {}
) {
    val refreshState = rememberPullToRefreshState()

    PullToRefreshBox(
        modifier = modifier,
        isRefreshing = isRefreshing,
        state = refreshState,
        onRefresh = onRefresh,
        indicator = {
            Indicator(
                modifier = Modifier.align(Alignment.TopCenter),
                isRefreshing = isRefreshing,
                containerColor = White80,
                color = Gray,
                state = refreshState
            )
        }
    ) {
        content()
    }
}