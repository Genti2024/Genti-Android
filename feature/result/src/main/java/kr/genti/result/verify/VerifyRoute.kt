package kr.genti.result.verify

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.dialog.GentiWarningDialog

@Composable
internal fun VerifyRoute(
    paddingValues: PaddingValues,
    viewModel: VerifyViewModel = hiltViewModel(),
    navigateToBack: () -> Unit = {},
) {
    val verifyState by viewModel.verifyState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(viewModel.verifySideEffect, lifecycleOwner) {
        viewModel.verifySideEffect.collect { sideEffect ->
            when (sideEffect) {
                is VerifySideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))
                is VerifySideEffect.NavigateToBack -> navigateToBack()
            }
        }
    }

    BackHandler {
        if (verifyState.isPhotoTaken) {
            viewModel.onIntent(VerifyIntent.BackButtonClick)
        } else {
            viewModel.onIntent(VerifyIntent.ExitButtonClick)
        }
    }

    if (!verifyState.isPhotoTaken) {
        VerifyBeforeScreen(
            modifier = Modifier,
            paddingValues = paddingValues,
            onBackButtonClicked = { viewModel.onIntent(VerifyIntent.ExitButtonClick) },
            onVerifyButtonClicked = { viewModel.onIntent(VerifyIntent.VerifyButtonClick) }
        )
    } else {
        VerifyAfterScreen(
            modifier = Modifier,
            paddingValues = paddingValues,
            imageUri = verifyState.imageUri,
            isLoading = verifyState.isLoading,
            onBackButtonClicked = { viewModel.onIntent(VerifyIntent.BackButtonClick) },
            onRetakeButtonClicked = { viewModel.onIntent(VerifyIntent.RetakeButtonClick) },
            onFinishButtonClicked = { viewModel.onIntent(VerifyIntent.FinishButtonClick) }
        )
    }

    if (verifyState.isExitDialogVisible) {
        GentiWarningDialog(
            titleRes = R.string.verify_exit_tv_title,
            subtitleRes = R.string.verify_exit_tv_subtitle,
            btnTextRes = R.string.verify_exit_btn_exit,
            onDismissRequest = { viewModel.onIntent(VerifyIntent.ExitDialogDismiss) },
            onBtnClick = { viewModel.onIntent(VerifyIntent.ExitButtonClick) }
        )
    }
}