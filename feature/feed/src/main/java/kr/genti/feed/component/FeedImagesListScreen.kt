package kr.genti.feed.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.genti.designsystem.component.layout.GentiTopBottomShadow
import kr.genti.domain.entity.response.FeedItemModel

@Composable
fun FeedImagesListScreen(
    modifier: Modifier = Modifier,
    itemList: ImmutableList<FeedItemModel> = persistentListOf(),
    isTooltipClosed: Boolean = false,
    onInfoBtnClick: () -> Unit = {},
    onListScroll: () -> Unit = {}
) {
    val listState = rememberLazyListState()
    val currentTooltipClosed by rememberUpdatedState(newValue = isTooltipClosed)

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }.collect { offset ->
            if (offset > 1000 && !currentTooltipClosed) onListScroll()
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp),
    ) {
        item {
            FeedHeader(
                onInfoBtnClick = onInfoBtnClick,
                modifier = Modifier.statusBarsPadding()
            )
        }
        itemsIndexed(
            items = itemList,
            key = { _, model -> model.picture.id }
        ) { _, item ->
            FeedItem(item)
        }
    }

    GentiTopBottomShadow(
        listState = listState,
        modifier = Modifier.fillMaxSize()
    )
}