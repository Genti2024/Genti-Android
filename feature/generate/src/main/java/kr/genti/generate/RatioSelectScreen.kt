package kr.genti.generate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kr.genti.designsystem.theme.GentiTheme

@Composable
fun RatioSelectScreen(
    modifier: Modifier = Modifier,
    onBackBtnClick: () -> Unit = {},
    onNextBtnClick: () -> Unit = {},
) {

}

@Preview
@Composable
private fun RatioSelectScreenPreview() {
    GentiTheme {
        RatioSelectScreen()
    }
}