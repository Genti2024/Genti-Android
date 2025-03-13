package kr.genti.result.waiting

import android.Manifest.permission.POST_NOTIFICATIONS
import androidx.activity.compose.BackHandler
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
import kr.genti.common.manager.LauncherManager.rememberPermissionLauncher
import kr.genti.common.manager.PermissionManager.checkPermissionAndLaunch
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiTheme

@Composable
internal fun WaitingRoute(
    viewModel: WaitingViewModel = hiltViewModel(),
    isParentPic: Boolean = false,
    navigateToBack: () -> Unit = {},
) {
    val waitingState by viewModel.waitingState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val alarmPermissionLauncher = rememberPermissionLauncher {
        viewModel.onIntent(WaitingIntent.AlarmRequestGrant)
    }

    LaunchedEffect(viewModel.waitingSideEffect, lifecycleOwner) {
        viewModel.waitingSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is WaitingSideEffect.NavigateToBack -> navigateToBack()
                is WaitingSideEffect.StartPermissionLauncher -> {
                    checkPermissionAndLaunch(
                        permission = POST_NOTIFICATIONS,
                        context = context,
                        onPermissionGranted = { viewModel.onIntent(WaitingIntent.AlarmRequestGrant) },
                        onPermissionNotGranted = {
                            alarmPermissionLauncher.launch(POST_NOTIFICATIONS)
                        },
                        onPermissionAlreadyDenied = { intentToSetting ->
                            context.toast(context.getString(R.string.permission_to_setting))
                            context.startActivity(intentToSetting)
                        }
                    )
                }
            }
        }
    }

    BackHandler {
        if (waitingState.isAlarmDialogVisible) {
            viewModel.onIntent(WaitingIntent.AlarmDialogDismiss)
        } else {
            viewModel.onIntent(WaitingIntent.ReturnButtonClick)
        }
    }
}

@Composable
private fun WaitingScreen(
    modifier: Modifier = Modifier,
) {

}

@Preview
@Composable
private fun WaitingScreenPreview() {
    GentiTheme {
        WaitingScreen()
    }
}