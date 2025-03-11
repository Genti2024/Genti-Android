package kr.genti.generate

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
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
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.domain.enums.PictureNumber
import kr.genti.domain.enums.PictureRatio
import kr.genti.generate.component.GenerateProgressBar
import kr.genti.generate.model.GenerateStage

@Composable
internal fun GenerateRoute(
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

    BackHandler {
        viewModel.onIntent(GenerateIntent.BackBtnClick)
    }

    GenerateScreen(
        modifier = Modifier,
        isParentPic = generateState.isParentPic,
        currentStage = generateState.currentStage,
        currentStep = generateState.currentStep,
        progress = generateState.progress,
        pictureNumber = generateState.pictureNumber,
        pictureRatio = generateState.pictureRatio,
        onPictureNumberClick = { viewModel.onIntent(GenerateIntent.NumberSelect(it)) },
        onPictureRatioClick = { viewModel.onIntent(GenerateIntent.RatioSelect(it)) },
        onBackBtnClick = { viewModel.onIntent(GenerateIntent.BackBtnClick) },
        onNextBtnClick = { viewModel.onIntent(GenerateIntent.NextBtnClick) }
    )
}

@Composable
private fun GenerateScreen(
    modifier: Modifier = Modifier,
    isParentPic: Boolean = false,
    currentStage: GenerateStage = GenerateStage.INIT,
    currentStep: Int = 1,
    progress: Float = 0f,
    pictureNumber: PictureNumber = PictureNumber.NONE,
    pictureRatio: PictureRatio = PictureRatio.NONE,
    onPictureNumberClick: (PictureNumber) -> Unit = {},
    onPictureRatioClick: (PictureRatio) -> Unit = {},
    onNextBtnClick: () -> Unit = {},
    onBackBtnClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        GentiTopBar(
            titleText = stringResource(if (!isParentPic) R.string.create_tv_title else R.string.create_parent_tv_title),
            isGenerate = true,
            currentStep = currentStep,
            totalStep = if (!isParentPic) 3 else 4,
            onBackButtonClick = onBackBtnClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        GenerateProgressBar(progress = progress)

        when (currentStage) {
            GenerateStage.NUMBER_SELECT -> {
                NumberSelectScreen(
                    pictureNumber = pictureNumber,
                    onPictureNumberClick = onPictureNumberClick,
                    onNextBtnClick = onNextBtnClick
                )
            }

            GenerateStage.PROMPT_INPUT -> {
                PromptInputScreen()
            }

            GenerateStage.RATIO_SELECT -> {
                RatioSelectScreen(
                    pictureRatio = pictureRatio,
                    onPictureRatioClick = onPictureRatioClick,
                    onNextBtnClick = onNextBtnClick
                )
            }

            GenerateStage.IMAGE_SELECT -> {
                ImageSelectScreen()
            }

            else -> {}
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