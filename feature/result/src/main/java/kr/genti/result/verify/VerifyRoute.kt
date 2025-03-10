package kr.genti.result.verify

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

    if (!verifyState.isPhotoTaken) {
        VerifyBeforeScreen(
            modifier = Modifier,
            paddingValues = paddingValues,
            onBackButtonClicked = { viewModel.onIntent(VerifyIntent.BackButtonClick) },
            onVerifyButtonClicked = { viewModel.onIntent(VerifyIntent.VerifyButtonClick) }
        )
    } else {
        VerifyAfterScreen(
            modifier = Modifier,
            paddingValues = paddingValues,
        )
    }
}