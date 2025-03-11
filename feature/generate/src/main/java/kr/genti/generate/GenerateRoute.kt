package kr.genti.generate

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.layout.GentiTopBar
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.White20
import kr.genti.generate.model.GenerateStage

@Composable
internal fun GenerateRoute(
    paddingValues: PaddingValues,
    viewModel: GenerateViewModel = hiltViewModel(),
    isParentPic: Boolean = false,
    navigateToWaiting: () -> Unit = {},
    navigateToBack: () -> Unit = {},
) {
    val generateState by viewModel.generateState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(GenerateIntent.Init(isParentPic))
    }

    LaunchedEffect(viewModel.generateSideEffect, lifecycleOwner) {
        viewModel.generateSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is GenerateSideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))
                is GenerateSideEffect.NavigateToWaiting -> navigateToWaiting()
                is GenerateSideEffect.NavigateToBack -> navigateToBack()
            }
        }
    }

    GenerateScreen(
        modifier = Modifier,
        paddingValues = paddingValues,
        isParentPic = generateState.isParentPic,
        currentStage = generateState.currentStage,
        currentStep = generateState.currentStep,
        progress = generateState.progress
    )
}

@Composable
private fun GenerateScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    isParentPic: Boolean = false,
    currentStage: GenerateStage = GenerateStage.PROMPT_INPUT,
    currentStep: Int = 1,
    progress: Float = 0f,
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 300),
        label = ""
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .padding(paddingValues)
    ) {
        GentiTopBar(
            titleText = stringResource(if (!isParentPic) R.string.create_tv_title else R.string.create_parent_tv_title),
            isGenerate = true,
            currentStep = currentStep,
            totalStep = if (!isParentPic) 3 else 4
        )

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth(),
            color = GentiGreen,
            trackColor = White20,
            gapSize = 0.dp,
            drawStopIndicator = { }
        )

        when (currentStage) {
            GenerateStage.NUMBER_SELECT -> {
                NumberSelectScreen()
            }

            GenerateStage.PROMPT_INPUT -> {
                PromptInputScreen()
            }

            GenerateStage.RATIO_SELECT -> {
                RatioSelectScreen()
            }

            GenerateStage.IMAGE_SELECT -> {
                ImageSelectScreen()
            }
        }
    }
}

@Preview
@Composable
private fun GenerateScreenPreview() {
    GentiTheme {
        GenerateScreen(
            progress = 0.33F,
            isParentPic = false,
            currentStage = GenerateStage.PROMPT_INPUT
        )
    }
}

@Preview
@Composable
private fun GenerateParentScreenPreview() {
    GentiTheme {
        GenerateScreen(
            progress = 0.25F,
            isParentPic = true,
            currentStage = GenerateStage.NUMBER_SELECT
        )
    }
}