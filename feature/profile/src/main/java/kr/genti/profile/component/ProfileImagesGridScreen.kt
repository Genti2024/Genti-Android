package kr.genti.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kr.genti.domain.entity.response.ImageModel

@Composable
fun ProfileImagesGridScreen(
    modifier: Modifier = Modifier,
    itemList: ImmutableList<ImageModel> = persistentListOf(),
    isGenerating: Boolean = false,
    onGenerateBtnClick: () -> Unit = {},
    onLastColumnLoaded: () -> Unit = {}
) {
    val gridState = rememberLazyGridState()

    LaunchedEffect(gridState) {
        snapshotFlow { gridState.layoutInfo.visibleItemsInfo }
            .map { visibleItems ->
                visibleItems.lastOrNull()?.index ?: 0
            }
            .distinctUntilChanged()
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex >= itemList.size - 3) onLastColumnLoaded()
            }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        state = gridState,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        itemsIndexed(
            items = itemList,
            key = { _, model -> model.id }
        ) { _, item ->
            ProfileItem(item = item)
        }
        item {
            ProfileGenerateItem(
                isGenerating = isGenerating,
                onBtnClick = onGenerateBtnClick
            )
        }
    }
}