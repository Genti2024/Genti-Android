package kr.genti.generate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.GentiButton
import kr.genti.designsystem.component.button.GentiSelectButton
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.domain.enums.PictureNumber

@Composable
fun NumberSelectScreen(
    modifier: Modifier = Modifier,
    pictureNumber: PictureNumber = PictureNumber.NONE,
    onPictureNumberClick: (PictureNumber) -> Unit = {},
    onNextBtnClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.number_tv_title),
                style = GentiTheme.typography.title
            )

            Spacer(modifier = Modifier.height(30.dp))

            Row(
                modifier = Modifier
                    .padding(horizontal = 30.dp)
                    .padding(bottom = 100.dp)
            ) {
                GentiSelectButton(
                    text = stringResource(R.string.number_btn_one_title),
                    description = stringResource(R.string.number_btn_one_subtitle),
                    iconRes = R.drawable.img_number_one,
                    modifier = Modifier.weight(1f),
                    isSelected = pictureNumber == PictureNumber.ONE,
                    onBtnClick = { onPictureNumberClick(PictureNumber.ONE) }
                )

                Spacer(modifier = Modifier.width(8.dp))

                GentiSelectButton(
                    text = stringResource(R.string.number_btn_two_title),
                    description = stringResource(R.string.number_btn_two_subtitle),
                    iconRes = R.drawable.img_number_two,
                    modifier = Modifier.weight(1f),
                    isSelected = pictureNumber == PictureNumber.TWO,
                    onBtnClick = { onPictureNumberClick(PictureNumber.TWO) }
                )
            }
        }

        GentiButton(
            textRes = R.string.btn_next,
            isActive = pictureNumber != PictureNumber.NONE,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            onClick = onNextBtnClick
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF030F0F)
@Composable
private fun NumberSelectScreenPreview() {
    GentiTheme {
        NumberSelectScreen(
            pictureNumber = PictureNumber.ONE,
        )
    }
}