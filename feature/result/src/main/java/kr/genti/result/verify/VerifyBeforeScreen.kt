package kr.genti.result.verify

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.CloseButton
import kr.genti.designsystem.component.button.GentiGradationButton
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.White30
import kr.genti.designsystem.theme.White60

@Composable
internal fun VerifyBeforeScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    onBackButtonClicked: () -> Unit = {},
    onVerifyButtonClicked: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .padding(bottom = paddingValues.calculateBottomPadding())
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_verify_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Image(
            painter = painterResource(id = R.drawable.ic_verify),
            contentDescription = null,
            modifier = Modifier.align(Alignment.Center)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {

            CloseButton(onCloseBtnClicked = onBackButtonClicked)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                Text(
                    text = stringResource(R.string.verify_before_tv_title),
                    style = GentiTheme.typography.title,
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp,
                    lineHeight = 30.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.verify_before_tv_subtitle),
                    style = GentiTheme.typography.body2,
                    textAlign = TextAlign.Center,
                    color = White60
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = stringResource(R.string.verify_before_tv_baby),
                    style = GentiTheme.typography.body2,
                    textAlign = TextAlign.Center,
                    color = White30
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_tooltip_verify),
                    contentDescription = null,
                    modifier = Modifier.height(48.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                GentiGradationButton(
                    textRes = R.string.verify_before_btn_photo,
                    onClick = onVerifyButtonClicked
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(R.string.verify_before_tv_warning),
                    style = GentiTheme.typography.caption2,
                    textAlign = TextAlign.Center,
                    color = White30
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview
@Composable
fun VerifyBeforeScreenPreview() {
    GentiTheme {
        VerifyBeforeScreen()
    }
}