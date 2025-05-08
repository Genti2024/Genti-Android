package kr.genti.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import kr.genti.core.common.BuildConfig
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.dialog.GentiErrorDialog
import kr.genti.designsystem.component.dialog.GentiNotiDialog
import kr.genti.designsystem.component.dialog.GentiWarningDialog
import kr.genti.designsystem.component.snackbar.GentiTextSnackBar
import kr.genti.designsystem.event.LocalSnackBarTrigger
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.domain.enums.GenerateStatus
import kr.genti.main.component.GenerateForceButton
import kr.genti.main.component.GenerateSelectDialog
import kr.genti.main.component.MainBottomBar
import kr.genti.main.component.MainBottomBtn
import kr.genti.main.component.MainNavHost
import kr.genti.main.navigation.MainNavigator
import kr.genti.main.navigation.MainTab
import kr.genti.main.navigation.rememberMainNavigator

@Composable
internal fun MainRoute(
    navigator: MainNavigator = rememberMainNavigator(),
    intentData: String? = null,
    viewModel: MainViewModel = hiltViewModel()
) {
    val mainState by viewModel.mainState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val isPreview = LocalInspectionMode.current

    val verifyResult by navigator.verifyResultFlow.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    val snackBarHostState = remember { SnackbarHostState() }
    val showSnackBar: (Int) -> Unit = { resId ->
        coroutineScope.launch {
            snackBarHostState.currentSnackbarData?.dismiss()
            snackBarHostState.showSnackbar(
                message = context.getString(resId)
            )
        }
    }

    LaunchedEffect(viewModel.mainSideEffect, lifecycleOwner) {
        viewModel.mainSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is MainSideEffect.ShowErrorToast -> showSnackBar(R.string.error_msg)
                is MainSideEffect.ShowStatusChangedToast -> showSnackBar(R.string.toast_state_changed)
                is MainSideEffect.NavigateToTab -> navigator.navigate(sideEffect.tab)
                is MainSideEffect.NavigateToVerify -> navigator.navigateToVerify()
                is MainSideEffect.NavigateToWaiting -> navigator.navigateToWaiting(sideEffect.isParentPic)
                is MainSideEffect.NavigateToGenerate -> navigator.navigateToGenerate(sideEffect.isParentPic)
                is MainSideEffect.NavigateToFinished -> navigator.navigateToFinished(
                    responseId = sideEffect.responseId,
                    imageUrl = sideEffect.imageUrl,
                    isGaro = sideEffect.isGaro,
                    isParentPic = sideEffect.isParentPic
                )
            }
        }
    }

    LaunchedEffect(intentData) {
        viewModel.onIntent(MainIntent.PushAlarmReceived(intentData))
    }

    LaunchedEffect(verifyResult) {
        if (verifyResult == true) {
            viewModel.onIntent(MainIntent.GenerateBtnClick)
            navigator.removeBackStackEntry()
        }
    }

    MainScreen(
        navigator = navigator,
        currentGenerateStatus = mainState.currentGenerateStatus,
        isPreview = isPreview,
        snackBarHostState = snackBarHostState,
        onShowSnackBar = showSnackBar,
        onTabSelected = { tab -> viewModel.onIntent(MainIntent.TabSelect(tab)) },
        onGenerateBtnClicked = { viewModel.onIntent(MainIntent.GenerateBtnClick) },
        onDebugPatchBtnClicked = { viewModel.onIntent(MainIntent.DebugPatchBtnClick) }
    )

    if (mainState.isUnableDialogVisible) {
        GentiWarningDialog(
            titleRes = R.string.unable_tv_title,
            subtitleRes = R.string.unable_tv_subtitle,
            errorMessage = mainState.serverUnableMessage,
            btnTextRes = R.string.btn_close,
            isOneButton = true,
            onDismissRequest = { viewModel.onIntent(MainIntent.DialogDismiss) }
        )
    }

    if (mainState.isErrorDialogVisible) {
        GentiErrorDialog(
            titleRes = R.string.waiting_error_tv_title,
            subtitleRes = R.string.waiting_error_tv_subtitle,
            btnTextRes = R.string.waiting_error_btn_again,
            onBtnClick = { viewModel.onIntent(MainIntent.RegenerateDialogBtnClick) },
            onDismissRequest = { viewModel.onIntent(MainIntent.DialogDismiss) }
        )
    }

    if (mainState.isFinishedDialogVisible) {
        GentiNotiDialog(
            iconRes = R.drawable.img_shine,
            titleRes = R.string.main_dialog_tv_title,
            subtitleRes = R.string.main_dialog_tv_subtitle,
            btnTextRes = R.string.main_dialog_btn_move_to_finish,
            onBtnClick = { viewModel.onIntent(MainIntent.FinishedDialogBtnClick) },
            onDismissRequest = { viewModel.onIntent(MainIntent.DialogDismiss) },
        )
    }

    if (mainState.isSelectDialogVisible) {
        GenerateSelectDialog(
            onBtnClick = { viewModel.onIntent(MainIntent.SelectDialogBtnClick(it)) },
            onDismissRequest = { viewModel.onIntent(MainIntent.DialogDismiss) },
        )
    }
}

@Composable
private fun MainScreen(
    modifier: Modifier = Modifier,
    navigator: MainNavigator = rememberMainNavigator(),
    currentGenerateStatus: GenerateStatus = GenerateStatus.EMPTY,
    isPreview: Boolean = LocalInspectionMode.current,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onShowSnackBar: (Int) -> Unit = {},
    onTabSelected: (MainTab) -> Unit = {},
    onGenerateBtnClicked: () -> Unit = {},
    onDebugPatchBtnClicked: () -> Unit = {}
) {

    Box(
        modifier.fillMaxSize()
    ) {
        CompositionLocalProvider(
            LocalSnackBarTrigger provides onShowSnackBar,
        ) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackBarHostState) {
                        GentiTextSnackBar(it.visuals.message)
                    }
                },
                bottomBar = {
                    MainBottomBar(
                        visible = isPreview || navigator.shouldShowBottomBar(),
                        tabs = MainTab.entries.toImmutableList(),
                        currentTab = navigator.currentTab,
                        onTabSelected = onTabSelected
                    )
                },
                content = { paddingValues ->
                    if (isPreview) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Black)
                        )
                    } else {
                        MainNavHost(
                            paddingValues = paddingValues,
                            navigator = navigator,
                            modifier = Modifier.background(Black),
                            startNavigateToGenerate = onGenerateBtnClicked
                        )
                    }
                }
            )
        }

        GenerateForceButton(
            isVisible = BuildConfig.DEBUG && currentGenerateStatus == GenerateStatus.IN_PROGRESS && (isPreview || navigator.shouldShowBottomBar()),
            onDebugPatchBtnClicked = onDebugPatchBtnClicked,
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        MainBottomBtn(
            visible = isPreview || navigator.shouldShowBottomBar(),
            onButtonClick = onGenerateBtnClicked,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    GentiTheme {
        MainScreen(
            currentGenerateStatus = GenerateStatus.IN_PROGRESS
        )
    }
}