package kr.genti.generate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.GentiButton
import kr.genti.designsystem.component.item.GentiCheckedText
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.domain.entity.response.ImageFileModel
import kr.genti.generate.component.ImagePersonItem

@Composable
fun ImageSixSelectScreen(
    modifier: Modifier = Modifier,
    imageList: List<ImageFileModel> = listOf(),
    extraImageList: List<ImageFileModel> = listOf(),
    onImageSelectBtnClick: () -> Unit = {},
    onExtraImageSelectBtnClick: () -> Unit = {},
    onNextBtnClick: () -> Unit = {},
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.selfie_tv_title_parent_two),
                style = GentiTheme.typography.title,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            GentiCheckedText(text = stringResource(R.string.selfie_tv_guide_1))
            GentiCheckedText(text = stringResource(R.string.selfie_tv_guide_parent))

            Spacer(modifier = Modifier.height(30.dp))

            ImagePersonItem(
                title = stringResource(R.string.selfie_tv_first_parent),
                imageList = imageList,
                onBtnClick = onImageSelectBtnClick
            )

            Spacer(modifier = Modifier.height(8.dp))

            ImagePersonItem(
                title = stringResource(R.string.selfie_tv_second_parent),
                imageList = extraImageList,
                onBtnClick = onExtraImageSelectBtnClick
            )
        }

        GentiButton(
            textRes = R.string.selfie_btn_create,
            isActive = imageList.size == 3 && extraImageList.size == 3,
            onClick = onNextBtnClick,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF030F0F)
@Composable
private fun ImageSixSelectScreenPreview() {
    GentiTheme {
        ImageSixSelectScreen()
    }
}