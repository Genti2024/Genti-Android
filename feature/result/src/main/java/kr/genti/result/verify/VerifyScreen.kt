package kr.genti.result.verify

import androidx.compose.foundation.layout.PaddingValues
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
import kr.genti.designsystem.theme.GentiTheme

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

    VerifyScreen(
        modifier = Modifier,
        paddingValues = paddingValues,
    )
}

@Composable
private fun VerifyScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
) {

}

@Preview
@Composable
fun VerifyScreenPreview() {
    GentiTheme {
        VerifyScreen()
    }
}