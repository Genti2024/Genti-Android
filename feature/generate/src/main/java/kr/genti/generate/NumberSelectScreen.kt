package kr.genti.generate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kr.genti.designsystem.theme.GentiTheme

@Composable
fun NumberSelectScreen(
    modifier: Modifier = Modifier,
    onBackBtnClick: () -> Unit = {},
    onNextBtnClick: () -> Unit = {},
) {

}

@Preview
@Composable
private fun NumberSelectScreenPreview() {
    GentiTheme {
        NumberSelectScreen()
    }
}