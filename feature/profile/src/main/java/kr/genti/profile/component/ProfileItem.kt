package kr.genti.profile.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kr.genti.designsystem.component.item.GentiAsyncImage
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio

@Composable
fun ProfileItem(
    modifier: Modifier = Modifier,
    item: ImageModel
) {
    Box(
        modifier = modifier.aspectRatio(1F)
    ) {
        GentiAsyncImage(
            url = item.url,
            isGaro = item.pictureRatio == PictureRatio.RATIO_GARO,
            isSquare = true
        )
    }
}

@Preview
@Composable
fun ProfileItemPreview() {
    GentiTheme {
        ProfileItem(
            item = ImageModel(
                id = 0,
                url = "",
                pictureRatio = PictureRatio.RATIO_SERO
            )
        )
    }
}