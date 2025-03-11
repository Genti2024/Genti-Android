package kr.genti.onboarding.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.extension.noRippleClickable
import kr.genti.common.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.GentiButton
import kr.genti.designsystem.component.button.GentiSelectButton
import kr.genti.designsystem.component.layout.GentiLoadingScreen
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiGradationEnd
import kr.genti.designsystem.theme.GentiGradationStart
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Transparent
import kr.genti.designsystem.theme.White40
import kr.genti.designsystem.theme.White80
import kr.genti.domain.enums.Gender

@Composable
internal fun SignupRoute(
    viewModel: SignupViewModel = hiltViewModel(),
    navigateToTutorial: () -> Unit = {},
) {
    val signupState by viewModel.signupState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(SignupIntent.Init)
    }

    LaunchedEffect(viewModel.signupSideEffect, lifecycleOwner) {
        viewModel.signupSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is SignupSideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))
                is SignupSideEffect.NavigateToTutorial -> navigateToTutorial()
            }
        }
    }

    LaunchedEffect(signupState.isFocusClearNeeded) {
        if (signupState.isFocusClearNeeded) {
            focusManager.clearFocus()
            viewModel.onIntent(SignupIntent.TextFieldFocused(false))
        }
    }

    SignupScreen(
        modifier = Modifier,
        isLoading = signupState.isLoading,
        selectedGender = signupState.selectedGender,
        selectedYear = signupState.selectedYear,
        isYearSelected = signupState.isYearSelected,
        isAllSelected = signupState.isAllSelected,
        onGenderClicked = { viewModel.onIntent(SignupIntent.GenderSelect(it)) },
        onSignupBtnClicked = { viewModel.onIntent(SignupIntent.SignupBtnClick) },
        onYearChanged = { viewModel.onIntent(SignupIntent.YearChange(it)) },
        onTextFieldOutsideClicked = { viewModel.onIntent(SignupIntent.TextFieldFocused(true)) }
    )
}

@Composable
private fun SignupScreen(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    selectedGender: Gender = Gender.NONE,
    selectedYear: String = "",
    isYearSelected: Boolean = false,
    isAllSelected: Boolean = false,
    onGenderClicked: (Gender) -> Unit = {},
    onYearChanged: (String) -> Unit = {},
    onSignupBtnClicked: () -> Unit = {},
    onTextFieldOutsideClicked: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .statusBarsPadding()
            .navigationBarsPadding()
            .noRippleClickable { onTextFieldOutsideClicked() }
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
                onGenderClick = onGenderClicked
            )

            Spacer(Modifier.height(20.dp))

            SignupBirthYearTextField(
                selectedYear = selectedYear,
                isYearSelected = isYearSelected,
                onYearChanged = onYearChanged,
            )

            Spacer(Modifier.height(60.dp))
        }

        GentiButton(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            textRes = R.string.signup_btn_submit,
            isActive = isAllSelected,
            onClick = onSignupBtnClicked
        )
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

@Composable
fun SignupBirthYearTextField(
    modifier: Modifier = Modifier,
    selectedYear: String = "",
    isYearSelected: Boolean = false,
    onYearChanged: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.signup_tv_birth_title),
            style = GentiTheme.typography.body1,
            color = GentiGreen,
        )

        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = selectedYear,
            onValueChange = { newValue ->
                if (newValue.length <= 4 && newValue.all { it.isDigit() }) onYearChanged(newValue)
            },
            textStyle = GentiTheme.typography.subtitle2.copy(textAlign = TextAlign.Center),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(Transparent),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedYear.isEmpty()) {
                        Text(
                            text = stringResource(id = R.string.signup_tv_birth_hint),
                            style = GentiTheme.typography.subtitle2,
                            color = White40
                        )
                    }
                    innerTextField()
                }
            },
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .offset(y = (-20).dp)
                .background(
                    brush = if (isYearSelected) {
                        Brush.linearGradient(listOf(GentiGradationStart, GentiGradationEnd))
                    } else {
                        SolidColor(White40)
                    },
                    shape = RoundedCornerShape(10.dp)
                )
        )
    }
}

@Preview
@Composable
fun SignupScreenPreview() {
    GentiTheme {
        SignupScreen()
    }
}