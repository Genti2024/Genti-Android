package kr.genti.setting

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jakewharton.processphoenix.ProcessPhoenix
import kr.genti.common.extension.toast
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.layout.GentiTopBar
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.White20
import kr.genti.designsystem.theme.White60
import kr.genti.feature.setting.BuildConfig
import kr.genti.setting.component.SettingItem

@Composable
internal fun SettingRoute(
    paddingValues: PaddingValues,
    viewModel: SettingViewModel = hiltViewModel(),
    navigateToBack: () -> Unit = {},
) {
    val settingState by viewModel.settingState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    LaunchedEffect(viewModel.settingSideEffect, lifecycleOwner) {
        viewModel.settingSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is SettingSideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))

                is SettingSideEffect.NavigateToBack -> navigateToBack()

                is SettingSideEffect.NavigateToWeb -> {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(sideEffect.url)))
                }

                is SettingSideEffect.RestartApp -> {
                    ProcessPhoenix.triggerRebirth(context)
                }
            }
        }
    }

    SettingScreen(
        modifier = Modifier,
        paddingValues = paddingValues,
        isLogoutDialogVisible = settingState.isLogoutDialogVisible,
        isQuitDialogVisible = settingState.isQuitDialogVisible,
        versionText = "v" + BuildConfig.VERSION_NAME,
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
    versionText: String = "",
    onBackButtonClick: () -> Unit = {},
    onTermButtonClick: () -> Unit = {},
    onPrivacyButtonClick: () -> Unit = {},
    onCompanyButtonClick: () -> Unit = {},
    onQuestionButtonClick: () -> Unit = {},
    onLogoutButtonClick: () -> Unit = {},
    onQuitButtonClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .padding(paddingValues)
    ) {
        GentiTopBar(
            titleText = stringResource(R.string.setting_tv_title),
            onBackButtonClick = onBackButtonClick,
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingItem(
            text = stringResource(R.string.setting_btn_terms_of_service),
            onItemClick = onTermButtonClick,
        )
        SettingItem(
            text = stringResource(R.string.setting_btn_privacy_policy),
            onItemClick = onPrivacyButtonClick,
        )
        SettingItem(
            text = stringResource(R.string.setting_btn_company_info),
            onItemClick = onCompanyButtonClick,
        )
        SettingItem(
            text = stringResource(R.string.setting_btn_question),
            onItemClick = onQuestionButtonClick,
        )
        SettingItem(
            text = stringResource(R.string.setting_btn_android_version),
            versionText = versionText,
            isArrowVisible = false,
        )

        HorizontalDivider(
            modifier = Modifier.padding(16.dp),
            thickness = 2.dp,
            color = White20
        )

        SettingItem(
            text = stringResource(R.string.setting_btn_logout),
            textColor = GentiGreen,
            isArrowVisible = false,
            onItemClick = onLogoutButtonClick,
        )
        SettingItem(
            text = stringResource(R.string.setting_btn_quit),
            textColor = White60,
            isArrowVisible = false,
            onItemClick = onQuitButtonClick,
        )
    }
}

@Preview
@Composable
fun SettingScreenPreview() {
    GentiTheme {
        SettingScreen(
            versionText = "v3.0.0",
        )
    }
}