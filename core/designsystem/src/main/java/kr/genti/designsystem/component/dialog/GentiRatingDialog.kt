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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.GentiButton
import kr.genti.designsystem.component.button.GentiGradationButton
import kr.genti.designsystem.component.item.GentiRatingBar
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Gray
import kr.genti.designsystem.theme.White
import kr.genti.designsystem.theme.White80

@Composable
fun GentiRatingDialog(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int = R.string.finished_rating_tv_title,
    @StringRes subtitleRes: Int = R.string.finished_rating_tv_subtitle,
    rating: Int = 5,
    onRatingChange: (Int) -> Unit = {},
    onSubmitBtnClick: () -> Unit = {},
    onSkipBtnClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
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
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(titleRes),
                    style = GentiTheme.typography.title,
                    color = White,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(subtitleRes),
                    style = GentiTheme.typography.body2,
                    color = White80,
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(20.dp))

                GentiRatingBar(
                    rating = rating,
                    onRatingChanged = onRatingChange,
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    GentiButton(
                        textRes = R.string.finished_rating_tv_btn_skip,
                        onClick = onSkipBtnClick,
                        btnColor = White,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    GentiGradationButton(
                        textRes = R.string.signup_btn_submit,
                        onClick = onSubmitBtnClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GentiRatingDialogPreview() {
    GentiTheme {
        GentiRatingDialog()
    }
}
