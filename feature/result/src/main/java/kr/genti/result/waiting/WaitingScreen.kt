package kr.genti.result.waiting

import android.Manifest.permission.POST_NOTIFICATIONS
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kr.genti.common.extension.toast
import kr.genti.common.manager.LauncherManager.rememberPermissionLauncher
import kr.genti.common.manager.PermissionManager.checkPermissionAndLaunch
import kr.genti.common.manager.PermissionManager.isPermissionGranted
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.GentiButton
import kr.genti.designsystem.component.dialog.GentiNotiDialog
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.White40
import kr.genti.designsystem.theme.White80

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

    LaunchedEffect(Unit) {
        viewModel.onIntent(WaitingIntent.Init(isParentPic))
    }

    LaunchedEffect(viewModel.waitingSideEffect, lifecycleOwner) {
        viewModel.waitingSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is WaitingSideEffect.NavigateToBack -> navigateToBack()

                is WaitingSideEffect.CheckPermission -> {
                    val isPermissionNeeded =
                        !isPermissionGranted(POST_NOTIFICATIONS, context)
                                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                    viewModel.onIntent(WaitingIntent.AlarmPermissionNeeded(isPermissionNeeded))
                }

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
                            viewModel.onIntent(WaitingIntent.NavigatedToSetting(true))
                        }
                    )
                }

                is WaitingSideEffect.GrantPermission -> {
                    context.toast(context.getString(R.string.push_success_toast))
                    navigateToBack()
                }
            }
        }
    }

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            if (waitingState.isNavigatedToSetting) {
                if (isPermissionGranted(POST_NOTIFICATIONS, context)) {
                    context.toast(context.getString(R.string.push_success_toast))
                    navigateToBack()
                } else {
                    viewModel.onIntent(WaitingIntent.NavigatedToSetting(false))
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

    WaitingScreen(
        isParentPic = waitingState.isParentPic,
        onReturnButtonClick = { viewModel.onIntent(WaitingIntent.ReturnButtonClick) },
    )

    if (waitingState.isAlarmDialogVisible) {
        GentiNotiDialog(
            iconRes = R.drawable.img_alarm,
            titleRes = R.string.push_tv_title,
            subtitleRes = R.string.push_tv_subtitle,
            btnTextRes = R.string.push_btn_get_alarm,
            isDismissLogicNeeded = true,
            onBtnClick = { viewModel.onIntent(WaitingIntent.AlarmDialogRequestButtonClick) },
            onDismissBtnClick = { viewModel.onIntent(WaitingIntent.AlarmDialogReturnButtonClick) },
            onDismissRequest = { viewModel.onIntent(WaitingIntent.AlarmDialogDismiss) },
        )
    }
}

@Composable
private fun WaitingScreen(
    modifier: Modifier = Modifier,
    isParentPic: Boolean = false,
    onReturnButtonClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .background(Black)
    ) {
        WaitingTopContent(
            modifier = Modifier.align(Alignment.TopCenter),
            isParentPic = isParentPic,
        )

        Image(
            painter = painterResource(R.drawable.img_glow),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )

        WaitingLottie(
            modifier = Modifier.align(Alignment.Center),
        )

        WaitingBottomContent(
            modifier = Modifier.align(Alignment.BottomCenter),
            onReturnButtonClick = onReturnButtonClick,
        )
    }
}

@Composable
private fun WaitingTopContent(
    modifier: Modifier = Modifier,
    isParentPic: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = stringResource(id = R.string.wait_tv_header),
            style = GentiTheme.typography.body1,
            color = GentiGreen,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = if (isParentPic) R.string.wait_tv_title_paid else R.string.wait_tv_title),
            style = GentiTheme.typography.title,
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            lineHeight = 30.sp,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = stringResource(id = R.string.wait_tv_subtitle),
            style = GentiTheme.typography.body2,
            color = White80,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun WaitingLottie(
    modifier: Modifier = Modifier,
) {
    val lottieComposition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.lottie_waiting)
    )
    Box(
        modifier = modifier
            .padding(horizontal = 60.dp)
            .padding(bottom = 30.dp)
            .aspectRatio(1F)
    ) {
        LottieAnimation(
            composition = lottieComposition,
            iterations = LottieConstants.IterateForever,
        )
    }
}

@Composable
private fun WaitingBottomContent(
    modifier: Modifier = Modifier,
    onReturnButtonClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        Text(
            text = stringResource(id = R.string.wait_tv_time_title),
            style = GentiTheme.typography.body1,
            color = White40,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = stringResource(id = R.string.wait_tv_time_subtitle),
            style = GentiTheme.typography.subtitle1,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.wait_tv_time_guide),
            style = GentiTheme.typography.body2,
            color = White80,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(16.dp))

        GentiButton(
            textRes = R.string.wait_btn_return,
            onClick = onReturnButtonClick,
            modifier = Modifier.padding(vertical = 16.dp)
        )
    }
}

@Preview
@Composable
private fun WaitingScreenPreview() {
    GentiTheme {
        WaitingScreen()
    }
}

@Preview
@Composable
private fun WaitingScreenParentPreview() {
    GentiTheme {
        WaitingScreen(
            isParentPic = true,
        )
    }
}