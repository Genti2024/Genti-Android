package kr.genti.onboarding.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kr.genti.common.xml.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiTheme

@Composable
internal fun SplashRoute(
    viewModel: SplashViewModel = hiltViewModel()
) {
    val feedState by viewModel.splashState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(SplashIntent.Init)
    }

    LaunchedEffect(viewModel.splashSideEffect, lifecycleOwner) {
        viewModel.splashSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is SplashSideEffect.ShowErrorToast -> {
                    context.toast(context.getString(R.string.error_msg))
                }

                is SplashSideEffect.NavigateToLogin -> {}
                is SplashSideEffect.NavigateToFeed -> {}
            }
        }
    }

    SplashScreen(modifier = Modifier)
}

@Composable
private fun SplashScreen(
    modifier: Modifier = Modifier,
) {
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_splash)
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
        )
    }

}

@Preview
@Composable
fun SplashScreenPreview() {
    GentiTheme {
        SplashScreen()
    }
}