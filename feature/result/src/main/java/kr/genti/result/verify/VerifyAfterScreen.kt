package kr.genti.result.verify

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.CloseButton
import kr.genti.designsystem.component.button.GentiButton
import kr.genti.designsystem.component.item.GentiAsyncUriImage
import kr.genti.designsystem.component.layout.GentiLoadingScreen
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Gray
import kr.genti.designsystem.theme.White
import kr.genti.designsystem.theme.White60

@Composable
internal fun VerifyAfterScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(),
    imageUri: Uri? = null,
    isLoading: Boolean = false,
    onBackButtonClicked: () -> Unit = {},
    onRetakeButtonClicked: () -> Unit = {},
    onFinishButtonClicked: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Black)
            .padding(paddingValues)
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
                text = stringResource(R.string.verify_after_tv_title),
                style = GentiTheme.typography.title,
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                lineHeight = 30.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.verify_after_tv_subtitle),
                style = GentiTheme.typography.body2,
                textAlign = TextAlign.Center,
                color = White60
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .aspectRatio(3F / 4F)
                .background(Gray)
                .border(2.dp, GentiGreen, RectangleShape)
                .align(Alignment.Center)
        ) {
            GentiAsyncUriImage(imageUri = imageUri)

            Text(
                text = stringResource(R.string.verify_after_tv_guide),
                style = GentiTheme.typography.body2,
                textAlign = TextAlign.Center,
                color = GentiGreen,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 20.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GentiButton(
                textRes = R.string.verify_after_btn_retake,
                btnColor = White,
                onClick = onRetakeButtonClicked
            )

            Spacer(modifier = Modifier.height(16.dp))

            GentiButton(
                textRes = R.string.verify_after_btn_verify,
                onClick = onFinishButtonClicked
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    GentiLoadingScreen(
        isLoading = isLoading,
        modifier = Modifier.fillMaxSize()
    )
}

@Preview
@Composable
fun VerifyAfterScreenPreview() {
    GentiTheme {
        VerifyAfterScreen()
    }
}