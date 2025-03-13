package kr.genti.result.finished

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kr.genti.common.extension.noRippleClickable
import kr.genti.common.extension.toast
import kr.genti.common.manager.ImageManager.getImageChooserIntent
import kr.genti.common.manager.LauncherManager.rememberPermissionLauncher
import kr.genti.common.manager.PermissionManager
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.CloseButton
import kr.genti.designsystem.component.button.GentiGradationButton
import kr.genti.designsystem.component.dialog.GentiImageDetailDialog
import kr.genti.designsystem.component.dialog.GentiRatingDialog
import kr.genti.designsystem.component.dialog.GentiTextFieldDialog
import kr.genti.designsystem.component.item.GentiAsyncImage
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Gray
import kr.genti.designsystem.theme.White40
import kr.genti.designsystem.theme.White60
import kr.genti.result.component.BackgroundBlurImage

@Composable
internal fun FinishedRoute(
    viewModel: FinishedViewModel = hiltViewModel(),
    responseId: Long = -1,
    imageUrl: String = "",
    isGaro: Boolean = false,
    isParentPic: Boolean = false,
    navigateToBack: () -> Unit = {},
) {
    val finishedState by viewModel.finishedState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val writePermissionLauncher = rememberPermissionLauncher {
        viewModel.onIntent(FinishedIntent.DownloadButtonClick)
    }

    LaunchedEffect(Unit) {
        viewModel.onIntent(FinishedIntent.Init(responseId, isParentPic, imageUrl))
    }

    LaunchedEffect(viewModel.finishedSideEffect, lifecycleOwner) {
        viewModel.finishedSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is FinishedSideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))
                is FinishedSideEffect.ShowDownloadToast -> context.toast(context.getString(R.string.profile_image_download_success))
                is FinishedSideEffect.NavigateToBack -> navigateToBack()
                is FinishedSideEffect.StartPermissionLauncher -> {
                    PermissionManager.checkPermissionAndLaunch(
                        permission = WRITE_EXTERNAL_STORAGE,
                        context = context,
                        onPermissionGranted = { viewModel.onIntent(FinishedIntent.DownloadButtonClick) },
                        onPermissionNotGranted = {
                            writePermissionLauncher.launch(WRITE_EXTERNAL_STORAGE)
                        },
                        onPermissionAlreadyDenied = { intentToSetting ->
                            context.toast(context.getString(R.string.permission_to_setting))
                            context.startActivity(intentToSetting)
                        }
                    )
                }

                is FinishedSideEffect.NavigateToShare -> {
                    context.startActivity(getImageChooserIntent(sideEffect.imageUri))
                }
            }
        }
    }

    BackHandler {
        if (finishedState.isDetailDialogVisible || finishedState.isRatingDialogVisible || finishedState.isReportDialogVisible) {
            viewModel.onIntent(FinishedIntent.DialogDismiss)
        } else {
            viewModel.onIntent(FinishedIntent.BackButtonClick)
        }
    }

    FinishedScreen(
        imageUrl = imageUrl,
        isParentPic = isParentPic,
        isGaro = isGaro,
        onBackButtonClick = { viewModel.onIntent(FinishedIntent.BackButtonClick) },
        onImageClick = { viewModel.onIntent(FinishedIntent.ImageClick) },
        onReportButtonClick = { viewModel.onIntent(FinishedIntent.ReportButtonClick) },
        onShareButtonClick = { viewModel.onIntent(FinishedIntent.ShareButtonClick) },
        onDownloadButtonClick = { viewModel.onIntent(FinishedIntent.DownloadButtonClick) },
    )

    if (finishedState.isDetailDialogVisible) {
        GentiImageDetailDialog(
            imageUrl = imageUrl,
            isGaro = isGaro,
            isOneButton = true,
            onSaveBtnClick = { viewModel.onIntent(FinishedIntent.DownloadButtonClick) },
            onDismissRequest = { viewModel.onIntent(FinishedIntent.DialogDismiss) }
        )
    }

    if (finishedState.isReportDialogVisible) {
        GentiTextFieldDialog(
            text = finishedState.reportText,
            isSubmitted = finishedState.isReportSubmitted,
            onTextChange = { viewModel.onIntent(FinishedIntent.ReportTextChange(it)) },
            onSubmitBtnClick = { viewModel.onIntent(FinishedIntent.ReportSubmitButtonClick) },
            onSubmitFinishBtnClick = { viewModel.onIntent(FinishedIntent.FinishButtonClick) },
            onDismissRequest = { viewModel.onIntent(FinishedIntent.DialogDismiss) }
        )
    }

    if (finishedState.isRatingDialogVisible) {
        GentiRatingDialog(
            rating = finishedState.rating,
            onRatingChange = { viewModel.onIntent(FinishedIntent.RatingChange(it)) },
            onSubmitBtnClick = { viewModel.onIntent(FinishedIntent.RatingSubmitButtonClick) },
            onSkipBtnClick = { viewModel.onIntent(FinishedIntent.RatingSkipButtonClick) },
            onDismissRequest = { viewModel.onIntent(FinishedIntent.DialogDismiss) }
        )
    }
}

@Composable
private fun FinishedScreen(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    isParentPic: Boolean = false,
    isGaro: Boolean = false,
    onBackButtonClick: () -> Unit = {},
    onImageClick: () -> Unit = {},
    onReportButtonClick: () -> Unit = {},
    onShareButtonClick: () -> Unit = {},
    onDownloadButtonClick: () -> Unit = {},
) {
    Box(
        modifier
            .fillMaxSize()
            .background(Black)
    ) {
        BackgroundBlurImage(
            imageUrl = imageUrl, isGaro = isGaro
        )

        CloseButton(
            modifier = Modifier.statusBarsPadding(),
            onCloseBtnClicked = onBackButtonClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Spacer(modifier = Modifier.weight(1f))

            FinishedTitle(
                isParentPic = isParentPic
            )

            Spacer(modifier = Modifier.weight(1f))

            FinishedImage(
                imageUrl = imageUrl,
                isGaro = isGaro,
                isParentPic = isParentPic,
                onImageClick = onImageClick,
                onDownloadButtonClick = onDownloadButtonClick
            )

            Spacer(modifier = Modifier.weight(1f))

            FinishedBottomContent(
                isParentPic = isParentPic,
                onReportButtonClick = onReportButtonClick,
                onShareButtonClick = onShareButtonClick,
                onDownloadButtonClick = onDownloadButtonClick
            )
        }
    }
}

@Composable
private fun FinishedTitle(
    modifier: Modifier = Modifier,
    isParentPic: Boolean = false,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        Text(
            text = stringResource(if (!isParentPic) R.string.finished_tv_title else R.string.finished_tv_title_paid),
            style = GentiTheme.typography.title,
            fontSize = 24.sp,
            lineHeight = 30.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.finished_tv_saved),
            style = GentiTheme.typography.body2,
            color = White60,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FinishedImage(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    isGaro: Boolean = false,
    isParentPic: Boolean = false,
    onImageClick: () -> Unit = {},
    onDownloadButtonClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = if (isGaro) 16.dp else 45.dp)
            .background(Gray, RoundedCornerShape(16.dp))
            .noRippleClickable { onImageClick() }
    ) {
        GentiAsyncImage(
            url = imageUrl,
            isGaro = isGaro,
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
        )
        if (!isParentPic) {
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.ic_download),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .noRippleClickable { onDownloadButtonClick() }
            )
        }
    }
}

@Composable
private fun FinishedBottomContent(
    modifier: Modifier = Modifier,
    isParentPic: Boolean = false,
    onReportButtonClick: () -> Unit = {},
    onShareButtonClick: () -> Unit = {},
    onDownloadButtonClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isParentPic) {
            Image(
                painter = painterResource(R.drawable.img_tooltip_finished),
                contentDescription = null,
                modifier = Modifier.width(260.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(32.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        GentiGradationButton(
            textRes = if (!isParentPic) R.string.finished_btn_share else R.string.finished_btn_save,
            onClick = if (!isParentPic) onShareButtonClick else onDownloadButtonClick
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .noRippleClickable { onReportButtonClick() },
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_info),
                contentDescription = null,
                tint = White40
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.finished_btn_unwanted),
                style = GentiTheme.typography.body1,
                color = White60
            )
        }
    }
}

@Preview
@Composable
private fun FinishedScreenPreview() {
    GentiTheme {
        FinishedScreen(isParentPic = false, isGaro = false)
    }
}

@Preview
@Composable
private fun FinishedScreenParentPreview() {
    GentiTheme {
        FinishedScreen(isParentPic = true, isGaro = true)
    }
}