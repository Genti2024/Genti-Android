package kr.genti.feed.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kr.genti.common.xml.extension.breakLines
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Gray
import kr.genti.designsystem.theme.White40
import kr.genti.designsystem.theme.White80
import kr.genti.domain.entity.response.FeedItemModel
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio

@Composable
internal fun FeedItem(feedItem: FeedItemModel) {
    var isImageLoaded by remember { mutableStateOf(false) }

    val aspectRatio = if (feedItem.picture.pictureRatio == PictureRatio.RATIO_GARO) {
        3f / 2f
    } else {
        2f / 3f
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Gray),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio)
            ) {
                AsyncImage(
                    model = feedItem.picture.url,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.matchParentSize(),
                    onSuccess = { isImageLoaded = true }
                )
                if (!isImageLoaded) {
                    val composition by rememberLottieComposition(
                        LottieCompositionSpec.RawRes(R.raw.lottie_loading_image)
                    )
                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.feed_tv_item_title),
                style = GentiTheme.typography.body2,
                color = White40,
                modifier = Modifier.padding(start = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = feedItem.prompt.breakLines(),
                style = GentiTheme.typography.body2,
                color = White80,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
private fun FeedItemPreview() {
    GentiTheme {
        FeedItem(
            feedItem = FeedItemModel(
                picture = ImageModel(
                    id = 1,
                    url = "https://images.unsplash.com/photo-168",
                    key = "123",
                    pictureRatio = PictureRatio.RATIO_GARO,
                    type = null
                ),
                prompt = "프랑스 야경을 즐기는 모습을 그려주세요. 항공점퍼를 입고 테라스에 서 있는 모습이에요."
            )
        )
    }
}