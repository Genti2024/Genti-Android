package kr.genti.generate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.item.GentiAsyncImage
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Gray
import kr.genti.designsystem.theme.White80

@Composable
internal fun PromptExampleItem(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    prompt: String = "",
) {
    val textLineHeight = with(LocalDensity.current) {
        GentiTheme.typography.body2.lineHeight.toDp()
    }
    val rowHeight = textLineHeight * 3 + 40.dp

    Row(
        modifier = modifier
            .padding(horizontal = 10.dp)
            .background(color = Gray, shape = RoundedCornerShape(8.dp))
            .fillMaxWidth()
            .height(rowHeight)
    ) {
        Box(
            modifier = Modifier
                .height(rowHeight)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp))
        ) {
            GentiAsyncImage(
                url = imageUrl,
                isSquare = true,
                modifier = Modifier.fillMaxSize()
            )
        }

        Text(
            text = prompt,
            style = GentiTheme.typography.body2,
            color = White80,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PromptExampleItemPreview() {
    GentiTheme {
        PromptExampleItem(
            imageUrl = "",
            prompt = stringResource(R.string.create_tv_example_1)
        )
    }
}