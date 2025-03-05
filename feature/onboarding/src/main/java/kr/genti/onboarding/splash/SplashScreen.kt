package kr.genti.onboarding.splash

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.xml.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiTheme

@Composable
internal fun SplashRoute(
    paddingValues: PaddingValues,
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

    SplashScreen(
        modifier = Modifier,
        innerPadding = paddingValues,
    )
}

@Composable
private fun SplashScreen(
    modifier: Modifier = Modifier,
    innerPadding: PaddingValues = PaddingValues(),
) {

}

@Preview
@Composable
fun SplashScreenPreview() {
    GentiTheme {
        SplashScreen()
    }
}