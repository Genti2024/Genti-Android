package kr.genti.result.finished

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
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.CloseButton
import kr.genti.designsystem.component.button.GentiGradationButton
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

    LaunchedEffect(Unit) {
        viewModel.onIntent(FinishedIntent.Init(responseId, imageUrl, isGaro, isParentPic))
    }

    LaunchedEffect(viewModel.finishedSideEffect, lifecycleOwner) {
        viewModel.finishedSideEffect.collect { sideEffect ->
            when (sideEffect) {
                is FinishedSideEffect.ShowErrorToast -> context.toast(context.getString(R.string.error_msg))
                is FinishedSideEffect.NavigateToBack -> navigateToBack()
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
}

@Composable
private fun FinishedScreen(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    isParentPic: Boolean = false,
    isGaro: Boolean = false,
    onBackButtonClick: () -> Unit = {},
) {
    Box(
        modifier
            .fillMaxSize()
            .background(Black)
    ) {
        BackgroundBlurImage(imageUrl = imageUrl)

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
            FinishedTitle(
                isParentPic = isParentPic
            )

            Spacer(modifier = Modifier.weight(1f))

            FinishedImage(
                imageUrl = imageUrl,
                isGaro = isGaro,
                isParentPic = isParentPic
            )

            Spacer(modifier = Modifier.weight(1f))

            FinishedBottomContent()
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
        Spacer(modifier = Modifier.height(70.dp))
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
        Image(
            painter = painterResource(R.drawable.img_tooltip_finished),
            contentDescription = null,
            modifier = Modifier.width(260.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        GentiGradationButton(
            textRes = R.string.btn_share,
            onClick = onShareButtonClick
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