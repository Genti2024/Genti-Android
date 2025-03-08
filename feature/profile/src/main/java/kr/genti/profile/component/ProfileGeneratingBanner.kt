package kr.genti.profile.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kr.genti.core.designsystem.R

@Composable
internal fun ProfileGenerationBanner(
    modifier: Modifier = Modifier,
    isGenerating: Boolean = false
) {
    if (isGenerating) {
        Image(
            painter = painterResource(R.drawable.img_profile_making),
            contentDescription = null,
            modifier = modifier.fillMaxWidth()
        )
    }
}