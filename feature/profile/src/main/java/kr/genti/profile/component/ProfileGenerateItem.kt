package kr.genti.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R

@Composable
internal fun ProfileGenerateItem(
    modifier: Modifier = Modifier,
    isGenerating: Boolean = false,
    onBtnClick: () -> Unit = {},
) {
    Image(
        painter = painterResource(id = if (!isGenerating) R.drawable.img_profile_create_active else R.drawable.img_profile_create_inactive),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .aspectRatio(1F)
            .noRippleClickable { onBtnClick() }
    )
}

@Preview
@Composable
private fun ProfileGenerateItemPreviewActive() {
    ProfileGenerateItem(
        isGenerating = false
    )
}

@Preview
@Composable
private fun ProfileGenerateItemPreviewInactive() {
    ProfileGenerateItem(
        isGenerating = true
    )
}