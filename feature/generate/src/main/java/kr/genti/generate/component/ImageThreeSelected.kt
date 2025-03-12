package kr.genti.generate.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.item.GentiAsyncImage
import kr.genti.designsystem.theme.Disabled
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.domain.entity.response.ImageFileModel

@Composable
internal fun ImageThreeSelected(
    modifier: Modifier = Modifier,
    imageList: List<ImageFileModel> = listOf(),
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        repeat(3) { index ->
            if (index > 0) {
                Spacer(modifier = Modifier.width(4.dp))
            }
            val image = imageList.getOrNull(index)
            if (image != null) {
                GentiAsyncImage(
                    url = image.url,
                    isSquare = true,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .border(1.dp, GentiGreen)
                )
            } else {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .border(1.dp, Disabled)
                ) {
                    Image(
                        painter = painterResource(R.drawable.img_empty_image),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ImageThreeSelectedPreview() {
    GentiTheme {
        ImageThreeSelected(
            imageList = listOf(
                ImageFileModel(-1, "", ""),
                ImageFileModel(-1, "", ""),
            )
        )
    }
}
