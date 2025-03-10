package kr.genti.designsystem.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.CloseButton
import kr.genti.designsystem.component.button.GentiButton
import kr.genti.designsystem.component.button.GentiGradationButton
import kr.genti.designsystem.component.item.GentiAsyncImage
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Transparent70
import kr.genti.designsystem.theme.White

@Composable
fun GentiImageDetailDialog(
    modifier: Modifier = Modifier,
    imageUrl: String = "",
    isGaro: Boolean = false,
    isOneButton: Boolean = false,
    onSaveBtnClick: () -> Unit = {},
    onShareBtnClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Transparent70)
                .noRippleClickable { onDismissRequest() }
        ) {
            CloseButton(
                onCloseBtnClicked = onDismissRequest
            )

            GentiAsyncImage(
                url = imageUrl,
                isGaro = isGaro,
                modifier = Modifier
                    .align(Alignment.Center)
                    .then(if (!isGaro) Modifier.padding(horizontal = 16.dp) else Modifier)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp)
            ) {
                if (isOneButton) {
                    GentiButton(
                        textRes = R.string.btn_download,
                        iconRes = R.drawable.ic_download_no_border,
                        onClick = onSaveBtnClick
                    )
                } else {
                    Row {
                        GentiGradationButton(
                            textRes = R.string.btn_share,
                            onClick = onShareBtnClick,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        GentiButton(
                            textRes = R.string.btn_download,
                            onClick = onSaveBtnClick,
                            btnColor = White,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun GentiImageDetailDialogPreviewOne() {
    GentiTheme {
        GentiImageDetailDialog(
            isGaro = true,
            isOneButton = true
        )
    }
}

@Preview
@Composable
private fun GentiImageDetailDialogPreviewTwo() {
    GentiTheme {
        GentiImageDetailDialog(
            isGaro = false,
            isOneButton = false
        )
    }
}
