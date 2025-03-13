package kr.genti.designsystem.component.dialog

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
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
import kr.genti.designsystem.theme.Transparent
import kr.genti.designsystem.theme.White
import kr.genti.designsystem.theme.White40
import kr.genti.designsystem.theme.White80

@Composable
fun GentiTextFieldDialog(
    modifier: Modifier = Modifier,
    @StringRes inputTitleRes: Int = R.string.finished_error_input_tv_title,
    @StringRes inputSubtitleRes: Int = R.string.finished_error_input_tv_subtitle,
    @StringRes inputHintRes: Int = R.string.finished_error_input_tv_hint,
    @StringRes outputTitleRes: Int = R.string.finished_error_output_tv_title,
    @StringRes outputSubtitleRes: Int = R.string.finished_error_output_tv_subtitle,
    text: String = "",
    isSubmitted: Boolean = false,
    onTextChange: (String) -> Unit = {},
    onSubmitBtnClick: () -> Unit = {},
    onSubmitFinishBtnClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        ),
    ) {
        val focusManager = LocalFocusManager.current

        Box(
            modifier = modifier
                .fillMaxSize()
                .imePadding()
                .noRippleClickable { focusManager.clearFocus() }
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .wrapContentSize()
                    .background(color = Gray, shape = RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(if (!isSubmitted) inputTitleRes else outputTitleRes),
                    style = GentiTheme.typography.title,
                    color = White,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(if (!isSubmitted) inputSubtitleRes else outputSubtitleRes),
                    style = GentiTheme.typography.body2,
                    color = White80,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (!isSubmitted) {
                    BasicTextField(
                        value = text,
                        onValueChange = { newValue -> onTextChange(newValue) },
                        textStyle = GentiTheme.typography.body2,
                        cursorBrush = SolidColor(Transparent),
                        decorationBox = { innerTextField ->
                            Box {
                                if (text.isEmpty()) {
                                    Text(
                                        text = stringResource(id = inputHintRes),
                                        style = GentiTheme.typography.body2,
                                        color = White40
                                    )
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Black, shape = RoundedCornerShape(10.dp))
                            .padding(16.dp)
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        GentiButton(
                            textRes = R.string.btn_close,
                            onClick = onDismissRequest,
                            btnColor = White,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        GentiButton(
                            textRes = R.string.finished_error_input_btn_submit,
                            onClick = onSubmitBtnClick,
                            isActive = text.isNotEmpty(),
                            modifier = Modifier.weight(1f)
                        )
                    }
                } else {
                    GentiButton(
                        textRes = R.string.btn_close,
                        onClick = onSubmitFinishBtnClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GentiTextFieldDialogInputPreview() {
    GentiTheme {
        GentiTextFieldDialog()
    }
}

@Preview(showBackground = true)
@Composable
private fun GentiTextFieldDialogOutputPreview() {
    GentiTheme {
        GentiTextFieldDialog(
            isSubmitted = true
        )
    }
}