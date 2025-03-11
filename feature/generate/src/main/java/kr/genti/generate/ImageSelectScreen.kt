package kr.genti.generate

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kr.genti.designsystem.theme.GentiTheme

@Composable
fun ImageSelectScreen(
    modifier: Modifier = Modifier,
    onNextBtnClick: () -> Unit = {},
) {

}

@Preview(showBackground = true, backgroundColor = 0xFF030F0F)
@Composable
private fun ImageSelectScreenPreview() {
    GentiTheme {
        ImageSelectScreen()
    }
}