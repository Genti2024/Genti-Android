package kr.genti.onboarding.login

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import kr.genti.common.xml.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.White80
import kr.genti.onboarding.component.KakaoLoginButton
import kr.genti.onboarding.component.MovingBackgroundImage

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
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        MovingBackgroundImage(imageRes = R.drawable.img_login_bg)

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(horizontal = 27.dp)
                .padding(top = 100.dp)
                .padding(top = paddingValues.calculateTopPadding()),
        ) {
            Text(
                text = stringResource(R.string.login_tv_title),
                style = GentiTheme.typography.title,
                fontSize = 24.sp,
                lineHeight = 32.sp,
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.login_tv_subtitle),
                style = GentiTheme.typography.body2,
                color = White80
            )
        }

        KakaoLoginButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 70.dp)
                .padding(horizontal = 16.dp),
            onBtnClick = onLoginBtnClicked
        )
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    GentiTheme {
        LoginScreen()
    }
}