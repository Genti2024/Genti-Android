package kr.genti.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Gray

@Composable
internal fun FeedRoute(
    paddingValues: PaddingValues,
    viewModel: FeedViewModel = hiltViewModel()
) {
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