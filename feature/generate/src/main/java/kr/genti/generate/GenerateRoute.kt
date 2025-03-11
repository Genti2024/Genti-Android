package kr.genti.generate

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.generate.model.GenerateStage

@Composable
internal fun GenerateRoute(
    paddingValues: PaddingValues,
    viewModel: GenerateViewModel = hiltViewModel(),
    navigateToWaiting: () -> Unit = {},
    navigateToBack: () -> Unit = {},
) {
    val generateState by viewModel.generateState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(GenerateIntent.Init)
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
        currentStage = generateState.currentStage,
    )
}

@Composable
private fun GenerateScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    currentStage: GenerateStage = GenerateStage.PROMPT_INPUT,
) {

}