package kr.genti.designsystem.component.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiTheme

@Composable
fun GentiAsyncImage(
    url: String,
    isGaro: Boolean,
    modifier: Modifier = Modifier
) {
    var isImageLoaded by remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_loading_image)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(if (isGaro) 3f / 2f else 2f / 3f),
        contentAlignment = Alignment.Center
    ) {
        if (LocalInspectionMode.current) {
            isImageLoaded = true
            Image(
                painter = painterResource(if (isGaro) R.drawable.mock_img_2_3 else R.drawable.mock_img_3_2),
                contentDescription = null,
                modifier = modifier.matchParentSize()
            )
        } else {
            AsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.matchParentSize(),
                onSuccess = { isImageLoaded = true }
            )
        }
    }

    if (!isImageLoaded) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(80.dp)
        )
    }
}

@Preview
@Composable
private fun GentiAsyncImagePreview() {
    GentiTheme {
        GentiAsyncImage(
            url = "",
            isGaro = true
        )
    }
}