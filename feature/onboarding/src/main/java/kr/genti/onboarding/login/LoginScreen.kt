package kr.genti.onboarding.login

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import kr.genti.common.xml.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiTheme

@Composable
internal fun LoginRoute(
    paddingValues: PaddingValues,
    viewModel: LoginViewModel = hiltViewModel(),
    navigateToSignup: () -> Unit = {},
    navigateToFeed: () -> Unit = {},
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(LoginIntent.Init)
    }

    LaunchedEffect(viewModel.loginSideEffect, lifecycleOwner) {
        viewModel.loginSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is LoginSideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))
                is LoginSideEffect.NavigateToSignup -> navigateToSignup()
                is LoginSideEffect.NavigateToFeed -> navigateToFeed()
                is LoginSideEffect.StartKakaoLogin -> {}
            }
        }
    }

    LoginScreen(
        modifier = Modifier,
        paddingValues = paddingValues,
        onLoginBtnClicked = { viewModel.onIntent(LoginIntent.LoginBtnClick) }
    )
}

@Composable
private fun LoginScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    onLoginBtnClicked: () -> Unit = {},
) {

}

@Preview
@Composable
fun LoginScreenPreview() {
    GentiTheme {
        LoginScreen()
    }
}