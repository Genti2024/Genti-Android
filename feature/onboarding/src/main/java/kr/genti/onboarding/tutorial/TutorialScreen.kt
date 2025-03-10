package kr.genti.onboarding.tutorial

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.navigation.OnboardingRoute
import kr.genti.navigation.Route

@Composable
internal fun TutorialRoute(
    paddingValues: PaddingValues,
    viewModel: TutorialViewModel = hiltViewModel(),
    navigateToFeed: (Route) -> Unit = {},
) {
    val tutorialState by viewModel.tutorialState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(viewModel.tutorialSideEffect, lifecycleOwner) {
        viewModel.tutorialSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is TutorialSideEffect.NavigateToFeed -> navigateToFeed(OnboardingRoute.Tutorial)
            }
        }
    }

    TutorialScreen(
        modifier = Modifier,
        paddingValues = paddingValues,
        onNextBtnClicked = { viewModel.onIntent(TutorialIntent.NextBtnClick) }
    )
}

@Composable
private fun TutorialScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    onNextBtnClicked: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {

    }
}

@Preview
@Composable
fun TutorialScreenPreview() {
    GentiTheme {
        TutorialScreen()
    }
}