package kr.genti.feed

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.genti.common.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.dialog.GentiBottomSheet
import kr.genti.designsystem.component.layout.GentiLoadingScreen
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.domain.entity.response.FeedItemModel
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio
import kr.genti.feed.component.FeedBottomTooltip
import kr.genti.feed.component.FeedImagesListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun FeedRoute(
    paddingValues: PaddingValues,
    viewModel: FeedViewModel = hiltViewModel()
) {
    val feedState by viewModel.feedState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(FeedIntent.Init)
    }

    LaunchedEffect(viewModel.feedSideEffect, lifecycleOwner) {
        viewModel.feedSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is FeedSideEffect.ShowErrorToast -> {
                    context.toast(context.getString(R.string.error_msg))
                }

                is FeedSideEffect.NavigateToWebsite -> {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(sideEffect.url)))
                }
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

    if (feedState.isBottomSheetVisible) {
        GentiBottomSheet(
            titleRes = R.string.feed_info_tv_title,
            subtitleRes = R.string.feed_info_tv_subtitle,
            btnRes = R.string.feed_info_btn_more,
            onDismissRequest = { viewModel.onIntent(FeedIntent.BottomSheetDismiss) },
            onBtnClick = { viewModel.onIntent(FeedIntent.MoreBtnClick) }
        )
    }
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
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .padding(bottom = innerPadding.calculateBottomPadding())
    ) {
        FeedImagesListScreen(
            innerPadding = innerPadding,
            itemList = itemList,
            isTooltipClosed = isTooltipClosed,
            onInfoBtnClick = onInfoBtnClick,
            onListScroll = onListScroll
        )

        FeedBottomTooltip(
            isTooltipVisible = isTooltipVisible,
            onTooltipClick = onTooltipClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        GentiLoadingScreen(
            isLoading = isLoading,
            modifier = Modifier.fillMaxSize()
        )
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