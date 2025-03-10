package kr.genti.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.xml.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme

@Composable
internal fun SettingRoute(
    paddingValues: PaddingValues,
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val settingState by viewModel.settingState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.onIntent(SettingIntent.Init)
    }

    LaunchedEffect(viewModel.settingSideEffect, lifecycleOwner) {
        viewModel.settingSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is SettingSideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))
                is SettingSideEffect.NavigateToWeb -> {}
                is SettingSideEffect.RestartApp -> {}
            }
        }
    }

    SettingScreen(
        modifier = Modifier,
        paddingValues = paddingValues,
        isLogoutDialogVisible = settingState.isLogoutDialogVisible,
        isQuitDialogVisible = settingState.isQuitDialogVisible,
        onBackButtonClick = { viewModel.onIntent(SettingIntent.BackButtonClick) },
        onTermButtonClick = { viewModel.onIntent(SettingIntent.TermButtonClick) },
        onPrivacyButtonClick = { viewModel.onIntent(SettingIntent.PrivacyButtonClick) },
        onCompanyButtonClick = { viewModel.onIntent(SettingIntent.CompanyButtonClick) },
        onQuestionButtonClick = { viewModel.onIntent(SettingIntent.QuestionButtonClick) },
        onLogoutButtonClick = { viewModel.onIntent(SettingIntent.LogoutButtonClick) },
        onQuitButtonClick = { viewModel.onIntent(SettingIntent.QuitButtonClick) },
    )
}

@Composable
private fun SettingScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    isLogoutDialogVisible: Boolean = false,
    isQuitDialogVisible: Boolean = false,
    onBackButtonClick: () -> Unit = {},
    onTermButtonClick: () -> Unit = {},
    onPrivacyButtonClick: () -> Unit = {},
    onCompanyButtonClick: () -> Unit = {},
    onQuestionButtonClick: () -> Unit = {},
    onLogoutButtonClick: () -> Unit = {},
    onQuitButtonClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .padding(paddingValues)
    ) {
        
    }
}

@Preview
@Composable
fun SettingScreenPreview() {
    GentiTheme {
        SettingScreen()
    }
}