package kr.genti.result.finished

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import kr.genti.common.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.Black

@Composable
internal fun FinishedRoute(
    viewModel: FinishedViewModel = hiltViewModel(),
    responseId: Long = -1,
    imageUrl: String = "",
    isGaro: Boolean = false,
    isParentPic: Boolean = false,
    navigateToBack: () -> Unit = {},
) {
    val finishedState by viewModel.finishedState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(FinishedIntent.Init(responseId, imageUrl, isGaro, isParentPic))
    }

    LaunchedEffect(viewModel.finishedSideEffect, lifecycleOwner) {
        viewModel.finishedSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is FinishedSideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))
                is FinishedSideEffect.NavigateToBack -> navigateToBack()
            }
        }
    }

    BackHandler {
        if (finishedState.isDetailDialogVisible || finishedState.isRatingDialogVisible || finishedState.isReportDialogVisible) {
            viewModel.onIntent(FinishedIntent.DialogDismiss)
        } else {
            viewModel.onIntent(FinishedIntent.BackButtonClick)
        }
    }
}

@Composable
private fun FinishedScreen(
    modifier: Modifier = Modifier,
    isParentPic: Boolean = false,
    isGaro: Boolean = false,
    navigateToBack: () -> Unit = {},
) {
    Box(
        modifier
            .fillMaxSize()
            .background(Black)
    ) {

    }
}

@Preview
@Composable
private fun FinishedScreenPreview() {
    FinishedScreen(isParentPic = false, isGaro = false)
}