package kr.genti.designsystem.component.dialog

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.GentiButton
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Gray
import kr.genti.designsystem.theme.Red
import kr.genti.designsystem.theme.Transparent30
import kr.genti.designsystem.theme.Transparent50
import kr.genti.designsystem.theme.White
import kr.genti.designsystem.theme.White80

@Composable
fun GentiWarningDialog(
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int,
    @StringRes btnTextRes: Int,
    modifier: Modifier = Modifier,
    isOneButton: Boolean = false,
    errorMessage: String? = null,
    onBtnClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Transparent50)
                .noRippleClickable { onDismissRequest() }
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .fillMaxWidth()
                    .background(color = Gray, shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_alert_triangle),
                    contentDescription = null,
                    modifier = Modifier.offset(y = (-8).dp)
                )

                Text(
                    text = stringResource(titleRes),
                    style = GentiTheme.typography.title,
                    color = White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.offset(y = (-12).dp)
                )

                Text(
                    text = errorMessage ?: stringResource(subtitleRes),
                    style = GentiTheme.typography.body2,
                    color = White80,
                    textAlign = TextAlign.Center,
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    if (isOneButton) {
                        GentiButton(
                            textRes = R.string.btn_close,
                            btnColor = White,
                            onClick = onDismissRequest,
                        )
                    } else {
                        Row {
                            GentiButton(
                                textRes = btnTextRes,
                                btnColor = Red,
                                textColor = White,
                                onClick = onBtnClick,
                                modifier = Modifier.weight(1f)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            GentiButton(
                                textRes = R.string.btn_return,
                                onClick = onDismissRequest,
                                btnColor = White,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GentiDialogPreviewOne() {
    GentiTheme {
        GentiWarningDialog(
            titleRes = R.string.unable_tv_title,
            subtitleRes = R.string.unable_tv_subtitle,
            btnTextRes = R.string.btn_close,
            isOneButton = true,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GentiDialogPreviewTwo() {
    GentiTheme {
        GentiWarningDialog(
            titleRes = R.string.setting_quit_tv_title,
            subtitleRes = R.string.setting_quit_tv_subtitle,
            btnTextRes = R.string.setting_quit_btn_quit,
        )
    }
}
