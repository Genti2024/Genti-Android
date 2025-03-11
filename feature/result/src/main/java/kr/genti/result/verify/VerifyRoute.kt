package kr.genti.result.verify

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.extension.toast
import kr.genti.common.manager.AmplitudeManager
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.dialog.GentiWarningDialog

@Composable
internal fun VerifyRoute(
    paddingValues: PaddingValues,
    viewModel: VerifyViewModel = hiltViewModel(),
    navigateToBack: () -> Unit = {},
) {
    val verifyState by viewModel.verifyState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) viewModel.onIntent(VerifyIntent.CameraPermissionGrant)
    }

    LaunchedEffect(viewModel.verifySideEffect, lifecycleOwner) {
        viewModel.verifySideEffect.collect { sideEffect ->
            when (sideEffect) {
                is VerifySideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))

                is VerifySideEffect.NavigateToBack -> navigateToBack()

                is VerifySideEffect.StartPermissionLauncher -> {
                    val hasCameraPermission = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED

                    if (hasCameraPermission) {
                        viewModel.onIntent(VerifyIntent.CameraPermissionGrant)
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }

                is VerifySideEffect.StartCameraLauncher -> {

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
            modifier = Modifier,
            paddingValues = paddingValues,
            onBackButtonClicked = { viewModel.onIntent(VerifyIntent.ExitButtonClick) },
            onVerifyButtonClicked = { viewModel.onIntent(VerifyIntent.VerifyButtonClick) }
        )
    } else {
        AmplitudeManager.trackEvent("view_verifyme2")
        VerifyAfterScreen(
            modifier = Modifier,
            paddingValues = paddingValues,
            imageUri = verifyState.imageUri,
            isLoading = verifyState.isLoading,
            onBackButtonClicked = { viewModel.onIntent(VerifyIntent.BackButtonClick) },
            onRetakeButtonClicked = { viewModel.onIntent(VerifyIntent.RetakeButtonClick) },
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