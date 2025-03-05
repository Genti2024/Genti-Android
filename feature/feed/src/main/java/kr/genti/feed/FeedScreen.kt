package kr.genti.feed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.genti.common.extension.noRippleClickable
import kr.genti.common.xml.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.layout.GentiLoadingScreen
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.domain.entity.response.FeedItemModel
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio
import kr.genti.feed.component.FeedHeader
import kr.genti.feed.component.FeedItem

@Composable
internal fun FeedRoute(
    paddingValues: PaddingValues,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    // 화면 초기화
    LaunchedEffect(Unit) {
        viewModel.onIntent(FeedIntent.Init)
    }

    // SideEffect 처리 (토스트 메시지)
    LaunchedEffect(viewModel.feedSideEffect, lifecycleOwner) {
        viewModel.feedSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is FeedSideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))
            }
        }
    }

    FeedScreen(
        modifier = Modifier,
        innerPadding = paddingValues,
        itemList = feedState.itemList,
        isTooltipVisible = feedState.isTooltipVisible,
        isTooltipClosed = feedState.isTooltipClosed,
        isLoading = feedState.isLoading,
        onInfoBtnClick = { viewModel.onIntent(FeedIntent.InfoBtnClick) },
        onTooltipClick = { viewModel.onIntent(FeedIntent.TooltipClick) },
        onListScroll = { viewModel.onIntent(FeedIntent.ListScroll) }
    )
}

@Composable
private fun FeedScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
    itemList: ImmutableList<FeedItemModel> = persistentListOf(),
    isTooltipVisible: Boolean = false,
    isTooltipClosed: Boolean = false,
    isLoading: Boolean = false,
    onInfoBtnClick: () -> Unit = {},
    onTooltipClick: () -> Unit = {},
    onListScroll: () -> Unit = {}
) {
    val listState = rememberLazyListState()
    val currentTooltipClosed by rememberUpdatedState(newValue = isTooltipClosed)

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }.collect { offset ->
            if (offset > 1000 && !currentTooltipClosed) onListScroll()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    FeedHeader(onInfoBtnClick = onInfoBtnClick)
                }
                itemsIndexed(
                    items = itemList,
                    key = { _, model -> model.picture.id }
                ) { _, item ->
                    FeedItem(item)
                }
            }

            Image(
                painter = painterResource(R.drawable.img_tooltip_feed),
                contentDescription = null,
                modifier = Modifier
                    .width(174.dp)
                    .padding(bottom = 20.dp)
                    .align(Alignment.BottomCenter)
                    .alpha(if (isTooltipVisible) 1f else 0f)
                    .noRippleClickable { onTooltipClick() }
            )
        }

        if (isLoading) {
            GentiLoadingScreen(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    GentiTheme {
        FeedScreen(
            itemList = persistentListOf(
                FeedItemModel(
                    picture = ImageModel(
                        id = 1,
                        url = "",
                        pictureRatio = PictureRatio.RATIO_GARO
                    ),
                    prompt = "사진 1"
                ),
                FeedItemModel(
                    picture = ImageModel(
                        id = 2,
                        url = "",
                        pictureRatio = PictureRatio.RATIO_SERO
                    ),
                    prompt = "사진 2"
                )
            )
        )
    }
}