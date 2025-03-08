package kr.genti.designsystem.component.item

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
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
    modifier: Modifier = Modifier,
    isSquare: Boolean = false
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_loading_image)
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(
                when {
                    isSquare -> 1F
                    isGaro -> 3F / 2F
                    else -> 2F / 3F
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        if (LocalInspectionMode.current) {
            Image(
                painter = painterResource(
                    if (isGaro) R.drawable.mock_img_2_3 else R.drawable.mock_img_3_2
                ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            SubcomposeAsyncImage(
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            ) {
                val state by painter.state.collectAsState()

                if (state is AsyncImagePainter.State.Loading) {
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier.size(80.dp)
                    )
                } else {
                    SubcomposeAsyncImageContent(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
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