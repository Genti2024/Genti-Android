package kr.genti.designsystem.component.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.Transparent50

@Composable
fun StempoLoadingScreen(
    modifier: Modifier = Modifier
) {
    val lottieLoading by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_loading)
    )

    Box(
        modifier = modifier,
    ) {
        LottieAnimation(
            composition = lottieLoading,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier
                .fillMaxSize()
                .background(Transparent50)
                .padding(horizontal = 50.dp)
                .noRippleClickable { }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StempoLoadingScreenPreview() {
    StempoLoadingScreen()
}