package kr.genti.generate

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.Companion.isPhotoPickerAvailable
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia.ImageOnly
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.genti.common.extension.stringOf
import kr.genti.common.extension.toast
import kr.genti.common.manager.LauncherManager.getMultipleGalleryPickerIntent
import kr.genti.common.manager.LauncherManager.rememberGalleryPickerLauncher
import kr.genti.common.manager.LauncherManager.rememberPhotoPickerLauncher
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.layout.GentiLoadingScreen
import kr.genti.designsystem.component.layout.GentiTopBar
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.domain.entity.response.ImageFileModel
import kr.genti.domain.entity.response.PromptExampleModel
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

    val photoPickerLauncher = rememberPhotoPickerLauncher(maxItems = 3) { uriList ->
        viewModel.onIntent(GenerateIntent.ImageSelect(uriList))
    }

    val galleryPickerLauncher = rememberGalleryPickerLauncher { uriList ->
        if (uriList.size > 3) context.toast(context.stringOf(R.string.selfie_toast_old_picker_limit))
        viewModel.onIntent(GenerateIntent.ImageSelect(uriList.take(3)))
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(GenerateIntent.Init(isParentPic))
    }

    LaunchedEffect(viewModel.generateSideEffect, lifecycleOwner) {
        viewModel.generateSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is GenerateSideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))
                is GenerateSideEffect.NavigateToWaiting -> navigateToWaiting()
                is GenerateSideEffect.NavigateToBack -> navigateToBack()

                is GenerateSideEffect.StartImageSelect -> {
                    if (isPhotoPickerAvailable(context) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        photoPickerLauncher.launch(PickVisualMediaRequest(ImageOnly))
                    } else {
                        galleryPickerLauncher.launch(getMultipleGalleryPickerIntent())
                    }
                }
            }
        }
    }

    BackHandler {
        viewModel.onIntent(GenerateIntent.BackBtnClick)
    }

    LaunchedEffect(generateState.isFocusClearNeeded) {
        if (generateState.isFocusClearNeeded) {
            focusManager.clearFocus()
            viewModel.onIntent(GenerateIntent.TextFieldFocused(false))
        }
    }

    GenerateScreen(
        modifier = Modifier,
        currentStage = generateState.currentStage,
        currentStep = generateState.currentStep,
        progress = generateState.progress,
        isParentPic = generateState.isParentPic,
        isBillingLoading = generateState.isBillingLoading,
        isRequestLoading = generateState.isRequestLoading,
        pictureNumber = generateState.pictureNumber,
        pictureRatio = generateState.pictureRatio,
        exampleList = generateState.exampleList,
        prompt = generateState.prompt,
        imageList = generateState.imageList,
        extraImageList = generateState.extraImageList,
        onPictureNumberClick = { viewModel.onIntent(GenerateIntent.NumberSelect(it)) },
        onExamplePageSwipe = { viewModel.onIntent(GenerateIntent.PromptExampleSwipe) },
        onPromptChanged = { viewModel.onIntent(GenerateIntent.PromptChange(it)) },
        onTextFieldOutsideClicked = { viewModel.onIntent(GenerateIntent.TextFieldFocused(true)) },
        onPictureRatioClick = { viewModel.onIntent(GenerateIntent.RatioSelect(it)) },
        onImageSelectBtnClick = { viewModel.onIntent(GenerateIntent.ImageSelectBtnClick(false)) },
        onExtraImageSelectBtnClick = { viewModel.onIntent(GenerateIntent.ImageSelectBtnClick(true)) },
        onBackBtnClick = { viewModel.onIntent(GenerateIntent.BackBtnClick) },
        onNextBtnClick = { viewModel.onIntent(GenerateIntent.NextBtnClick) }
    )
}

@Composable
private fun GenerateScreen(
    modifier: Modifier = Modifier,
    currentStage: GenerateStage = GenerateStage.INIT,
    currentStep: Int = 1,
    progress: Float = 0f,
    isParentPic: Boolean = false,
    isBillingLoading: Boolean = false,
    isRequestLoading: Boolean = false,
    pictureNumber: PictureNumber = PictureNumber.NONE,
    pictureRatio: PictureRatio = PictureRatio.NONE,
    exampleList: ImmutableList<PromptExampleModel> = persistentListOf(),
    prompt: String = "",
    imageList: List<ImageFileModel> = listOf(),
    extraImageList: List<ImageFileModel> = listOf(),
    onPictureNumberClick: (PictureNumber) -> Unit = {},
    onExamplePageSwipe: () -> Unit = {},
    onPromptChanged: (String) -> Unit = {},
    onTextFieldOutsideClicked: () -> Unit = {},
    onPictureRatioClick: (PictureRatio) -> Unit = {},
    onImageSelectBtnClick: () -> Unit = {},
    onExtraImageSelectBtnClick: () -> Unit = {},
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
                PromptInputScreen(
                    isParentPic = isParentPic,
                    exampleList = exampleList,
                    prompt = prompt,
                    onExamplePageSwipe = onExamplePageSwipe,
                    onPromptChanged = onPromptChanged,
                    onTextFieldOutsideClicked = onTextFieldOutsideClicked,
                    onNextBtnClick = onNextBtnClick
                )
            }

            GenerateStage.RATIO_SELECT -> {
                RatioSelectScreen(
                    pictureRatio = pictureRatio,
                    onPictureRatioClick = onPictureRatioClick,
                    onNextBtnClick = onNextBtnClick
                )
            }

            GenerateStage.IMAGE_THREE_SELECT -> {
                ImageThreeSelectScreen(
                    isParentPic = isParentPic,
                    imageList = imageList,
                    onImageSelectBtnClick = onImageSelectBtnClick,
                    onNextBtnClick = onNextBtnClick
                )
            }

            GenerateStage.IMAGE_SIX_SELECT -> {
                ImageSixSelectScreen(
                    imageList = imageList,
                    extraImageList = extraImageList,
                    onImageSelectBtnClick = onImageSelectBtnClick,
                    onExtraImageSelectBtnClick = onExtraImageSelectBtnClick,
                    onNextBtnClick = onNextBtnClick
                )
            }

            else -> {}
        }
    }

    GentiLoadingScreen(
        isLoading = isRequestLoading,
        rawRes = R.raw.lottie_loading_create,
        modifier = Modifier.fillMaxSize()
    )

    GentiLoadingScreen(
        isLoading = isBillingLoading,
        modifier = Modifier.fillMaxSize()
    )
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