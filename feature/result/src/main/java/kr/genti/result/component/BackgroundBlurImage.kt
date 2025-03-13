package kr.genti.result.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.Transparent50

@Composable
internal fun BackgroundBlurImage(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        val context = LocalContext.current

        val imageRequest = ImageRequest.Builder(context)
            .data(if (LocalInspectionMode.current) R.drawable.mock_img_3_2 else imageUrl)
            .build()

        Box(modifier = modifier.fillMaxSize()) {
            AsyncImage(
                model = imageRequest,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
                    .blur(radiusX = 50.dp, radiusY = 50.dp)
            )
        }
        Box(modifier = modifier
            .fillMaxSize()
            .background(Transparent50))
    }
}

@Preview
@Composable
private fun BackgroundBlurImagePreview() {
    BackgroundBlurImage()
}