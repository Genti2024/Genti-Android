package kr.genti.result.verify

import android.Manifest.permission.CAMERA
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.extension.toast
import kr.genti.common.manager.AmplitudeManager
import kr.genti.common.manager.LauncherManager.rememberPermissionLauncher
import kr.genti.common.manager.PermissionManager.checkPermissionAndLaunch
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.dialog.GentiWarningDialog

@Composable
internal fun VerifyRoute(
    viewModel: VerifyViewModel = hiltViewModel(),
    navigateToBack: (Boolean) -> Unit = {},
) {
    val verifyState by viewModel.verifyState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val cameraPermissionLauncher = rememberPermissionLauncher {
        viewModel.onIntent(VerifyIntent.CameraPermissionGrant)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) viewModel.onIntent(VerifyIntent.CameraResultSuccess)
    }

    LaunchedEffect(viewModel.verifySideEffect, lifecycleOwner) {
        viewModel.verifySideEffect.collect { sideEffect ->
            when (sideEffect) {
                is VerifySideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))

                is VerifySideEffect.NavigateToBack -> navigateToBack(false)

                is VerifySideEffect.StartPermissionLauncher -> {
                    checkPermissionAndLaunch(
                        permission = CAMERA,
                        context = context,
                        onPermissionGranted = { viewModel.onIntent(VerifyIntent.CameraPermissionGrant) },
                        onPermissionNotGranted = {
                            cameraPermissionLauncher.launch(CAMERA)
                        },
                        onPermissionAlreadyDenied = { intentToSetting ->
                            context.toast(context.getString(R.string.permission_to_setting))
                            context.startActivity(intentToSetting)
                        }
                    )
                }

                is VerifySideEffect.StartCameraLauncher -> {
                    verifyState.imageUri?.let { cameraLauncher.launch(it) }
                }

                is VerifySideEffect.VerifySuccess -> {
                    context.toast(context.getString(R.string.verify_success_toast))
                    navigateToBack(true)
                }
            }
        }
    }

    BackHandler {
        if (verifyState.isPhotoTaken) {
            viewModel.onIntent(VerifyIntent.BackButtonClick)
        } else {
            viewModel.onIntent(VerifyIntent.ExitButtonClick)
        }
    }

    if (!verifyState.isPhotoTaken) {
        AmplitudeManager.trackEvent("view_verifyme1")
        VerifyBeforeScreen(
            onBackButtonClicked = { viewModel.onIntent(VerifyIntent.ExitButtonClick) },
            onVerifyButtonClicked = { viewModel.onIntent(VerifyIntent.CameraButtonClick(true)) }
        )
    } else {
        AmplitudeManager.trackEvent("view_verifyme2")
        VerifyAfterScreen(
            imageUri = verifyState.imageUri,
            isLoading = verifyState.isLoading,
            onBackButtonClicked = { viewModel.onIntent(VerifyIntent.BackButtonClick) },
            onRetakeButtonClicked = { viewModel.onIntent(VerifyIntent.CameraButtonClick(false)) },
            onFinishButtonClicked = { viewModel.onIntent(VerifyIntent.FinishButtonClick) }
        )
    }

    if (verifyState.isExitDialogVisible) {
        GentiWarningDialog(
            titleRes = R.string.verify_exit_tv_title,
            subtitleRes = R.string.verify_exit_tv_subtitle,
            btnTextRes = R.string.verify_exit_btn_exit,
            onDismissRequest = { viewModel.onIntent(VerifyIntent.ExitDialogDismiss) },
            onBtnClick = { viewModel.onIntent(VerifyIntent.ExitButtonClick) }
        )
    }
}