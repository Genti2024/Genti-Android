package kr.genti.onboarding.signup

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.xml.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.layout.GentiLoadingScreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.navigation.OnboardingRoute
import kr.genti.navigation.Route

@Composable
internal fun SignupRoute(
    paddingValues: PaddingValues,
    viewModel: SignupViewModel = hiltViewModel(),
    navigateToTutorial: (Route) -> Unit = {},
) {
    val signupState by viewModel.signupState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(SignupIntent.Init)
    }

    LaunchedEffect(viewModel.signupSideEffect, lifecycleOwner) {
        viewModel.signupSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is SignupSideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))
                is SignupSideEffect.NavigateToTutorial -> navigateToTutorial(OnboardingRoute.Signup)
            }
        }
    }

    SignupScreen(
        modifier = Modifier,
        paddingValues = paddingValues,
        isLoading = signupState.isLoading,
        onSignupBtnClicked = { viewModel.onIntent(SignupIntent.SignupBtnClick) }
    )
}

@Composable
private fun SignupScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    isLoading: Boolean = false,
    onSignupBtnClicked: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {

    }

    GentiLoadingScreen(
        isLoading = isLoading,
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
fun SignupScreenPreview() {
    GentiTheme {
        SignupScreen()
    }
}