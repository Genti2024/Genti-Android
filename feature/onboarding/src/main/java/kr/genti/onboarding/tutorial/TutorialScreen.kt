package kr.genti.onboarding.tutorial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.GentiButton
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.White
import kr.genti.navigation.OnboardingRoute
import kr.genti.navigation.Route
import kr.genti.onboarding.model.TutorialStage

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
        currentStage = tutorialState.currentStage,
        onNextBtnClicked = { viewModel.onIntent(TutorialIntent.NextBtnClick) },
        onCloseBtnClicked = { viewModel.onIntent(TutorialIntent.CloseBtnClick) }
    )
}

@Composable
private fun TutorialScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    currentStage: TutorialStage = TutorialStage.FIRST,
    onNextBtnClicked: () -> Unit = {},
    onCloseBtnClicked: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
            contentDescription = null,
            tint = White,
            modifier = Modifier
                .padding(16.dp)
                .padding(top = 16.dp)
                .align(Alignment.TopEnd)
                .noRippleClickable { onCloseBtnClicked() }
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp)
                .padding(bottom = 20.dp)
        ) {
            GentiButton(
                textRes = if (currentStage != TutorialStage.THIRD) R.string.onboarding_btn_next else R.string.onboarding_btn_finish,
                onClick = onNextBtnClicked,
            )
        }
    }
}

@Preview
@Composable
fun TutorialScreenPreview() {
    GentiTheme {
        TutorialScreen()
    }
}