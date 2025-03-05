package kr.genti.feed

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.genti.common.xml.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.layout.GentiLoadingScreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.domain.entity.response.FeedItemModel

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
        currentPage = feedState.currentPage,
        itemList = feedState.itemList,
        isTooltipVisible = feedState.isTooltipVisible,
        isLoading = feedState.isLoading,
        onInfoBtnClick = { viewModel.onIntent(FeedIntent.InfoBtnClick) },
        onTooltipClick = { viewModel.onIntent(FeedIntent.TooltipClick) }
    )
}

@Composable
private fun FeedScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
    currentPage: Int = 0,
    itemList: ImmutableList<FeedItemModel> = persistentListOf(),
    isTooltipVisible: Boolean = true,
    isLoading: Boolean = false,
    onInfoBtnClick: () -> Unit = {},
    onTooltipClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {

        }

        if (isLoading) {
            GentiLoadingScreen(modifier = Modifier.fillMaxSize())
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    GentiTheme {
        FeedScreen()
    }
}