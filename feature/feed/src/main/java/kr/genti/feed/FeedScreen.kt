package kr.genti.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.xml.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Gray

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
        modifier = Modifier.padding(paddingValues)
    )
}

@Composable
private fun FeedScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Gray),
    ) {
        Text("Feed")
    }
}

@Preview(showBackground = true)
@Composable
private fun FeedScreenPreview() {
    GentiTheme {
        FeedScreen()
    }
}