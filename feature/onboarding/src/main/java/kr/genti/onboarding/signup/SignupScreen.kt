package kr.genti.onboarding.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.xml.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.GentiSelectButton
import kr.genti.designsystem.component.layout.GentiLoadingScreen
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.White80
import kr.genti.domain.enums.Gender
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
        selectedGender = signupState.selectedGender,
        onGenderClick = { viewModel.onIntent(SignupIntent.GenderSelect(it)) },
        onSignupBtnClicked = { viewModel.onIntent(SignupIntent.SignupBtnClick) }
    )
}

@Composable
private fun SignupScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    isLoading: Boolean = false,
    selectedGender: Gender = Gender.NONE,
    onGenderClick: (Gender) -> Unit = {},
    onSignupBtnClicked: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            SignupTitle()

            Spacer(Modifier.height(40.dp))

            SignupGenderSelect(
                selectedGender = selectedGender,
                onGenderClick = onGenderClick
            )
        }
    }

    GentiLoadingScreen(
        isLoading = isLoading,
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun SignupTitle(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.signup_tv_title),
            style = GentiTheme.typography.title,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.signup_tv_subtitle),
            style = GentiTheme.typography.body2,
            color = White80,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SignupGenderSelect(
    modifier: Modifier = Modifier,
    selectedGender: Gender = Gender.NONE,
    onGenderClick: (Gender) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.signup_tv_gender_title),
            style = GentiTheme.typography.body1,
            color = GentiGreen,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            GentiSelectButton(
                text = stringResource(R.string.signup_tv_gender_male),
                iconRes = R.drawable.ic_male,
                modifier = Modifier.weight(1f),
                isSelected = selectedGender == Gender.M,
                isTintNeeded = true,
                onBtnClick = { onGenderClick(Gender.M) }
            )

            Spacer(modifier = Modifier.width(8.dp))

            GentiSelectButton(
                text = stringResource(R.string.signup_tv_gender_female),
                iconRes = R.drawable.ic_female,
                modifier = Modifier.weight(1f),
                isSelected = selectedGender == Gender.W,
                isTintNeeded = true,
                onBtnClick = { onGenderClick(Gender.W) }
            )
        }
    }
}

@Preview
@Composable
fun SignupScreenPreview() {
    GentiTheme {
        SignupScreen()
    }
}