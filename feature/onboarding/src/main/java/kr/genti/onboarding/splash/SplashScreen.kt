package kr.genti.onboarding.splash

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jakewharton.processphoenix.ProcessPhoenix
import kr.genti.common.manager.AppUpdateManager
import kr.genti.common.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.navigation.OnboardingRoute
import kr.genti.navigation.Route

@Composable
internal fun SplashRoute(
    viewModel: SplashViewModel = hiltViewModel(),
    navigateToLogin: (Route) -> Unit = {},
    navigateToFeed: (Route) -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            ProcessPhoenix.triggerRebirth(context)
        } else {
            (context as? Activity)?.finishAffinity()
                ?: context.toast(context.getString(R.string.error_msg))
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(SplashIntent.Init)
    }

    LaunchedEffect(viewModel.splashSideEffect, lifecycleOwner) {
        viewModel.splashSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is SplashSideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))
                is SplashSideEffect.NavigateToLogin -> navigateToLogin(OnboardingRoute.Splash)
                is SplashSideEffect.NavigateToFeed -> navigateToFeed(OnboardingRoute.Splash)
                is SplashSideEffect.StartAppUpdate -> AppUpdateManager.startAppUpdate(launcher)
            }
        }
    }

    SplashScreen(
        modifier = Modifier,
        onLottiePlayFinished = { viewModel.onIntent(SplashIntent.LottiePlayFinish) }
    )
}

@Composable
private fun SplashScreen(
    modifier: Modifier = Modifier,
    onLottiePlayFinished: () -> Unit = {},
) {
    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_splash)
    )

    val lottieProgress by animateLottieCompositionAsState(
        composition = lottieComposition,
        iterations = 1
    )

    LaunchedEffect(lottieProgress) {
        if (lottieProgress >= 1f) onLottiePlayFinished()
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = lottieComposition,
            progress = { lottieProgress },
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
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