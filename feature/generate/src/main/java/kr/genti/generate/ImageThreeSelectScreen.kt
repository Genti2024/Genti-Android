package kr.genti.generate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import kr.genti.designsystem.theme.White
import kr.genti.designsystem.theme.White30
import kr.genti.domain.entity.response.ImageFileModel
import kr.genti.generate.component.ImageThreeExample
import kr.genti.generate.component.ImageThreeSelected

@Composable
fun ImageThreeSelectScreen(
    modifier: Modifier = Modifier,
    isParentPic: Boolean = false,
    imageList: List<ImageFileModel> = listOf(),
    onImageSelectBtnClick: () -> Unit = {},
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
                text = stringResource(
                    if (isParentPic) R.string.selfie_tv_title_parent_one else R.string.selfie_tv_title
                ),
                style = GentiTheme.typography.title,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            GentiCheckedText(text = stringResource(R.string.selfie_tv_guide_1))

            if (isParentPic) {
                GentiCheckedText(text = stringResource(R.string.selfie_tv_guide_parent))
            } else {
                GentiCheckedText(text = stringResource(R.string.selfie_tv_guide_2))
                GentiCheckedText(text = stringResource(R.string.selfie_tv_guide_3))

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.selfie_tv_warning),
                    style = GentiTheme.typography.caption2,
                    color = White30,
                    textAlign = TextAlign.Center
                )
            }

            if (imageList.isEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                ImageThreeExample(isParentPic = isParentPic)
            } else {
                Spacer(modifier = Modifier.height(36.dp))
                ImageThreeSelected(imageList = imageList)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            GentiButton(
                textRes = if (imageList.isEmpty()) R.string.selfie_tv_btn_select else R.string.selfie_tv_btn_reselect,
                btnColor = White,
                onClick = onImageSelectBtnClick
            )
            Spacer(modifier = Modifier.height(8.dp))
            GentiButton(
                textRes = R.string.selfie_btn_create,
                isActive = imageList.size == 3,
                onClick = onNextBtnClick
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF030F0F)
@Composable
private fun ImageThreeSelectScreenDefaultPreview() {
    GentiTheme {
        ImageThreeSelectScreen(isParentPic = false)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF030F0F)
@Composable
private fun ImageThreeSelectScreenParentPreview() {
    GentiTheme {
        ImageThreeSelectScreen(isParentPic = true)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF030F0F)
@Composable
private fun ImageThreeSelectScreenSelectedPreview() {
    GentiTheme {
        ImageThreeSelectScreen(
            isParentPic = false,
            imageList = listOf(
                ImageFileModel(-1, "", ""),
                ImageFileModel(-1, "", ""),
            )
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF030F0F)
@Composable
private fun ImageThreeSelectScreenAllSelectedPreview() {
    GentiTheme {
        ImageThreeSelectScreen(
            isParentPic = false,
            imageList = listOf(
                ImageFileModel(-1, "", ""),
                ImageFileModel(-1, "", ""),
                ImageFileModel(-1, "", ""),
            )
        )
    }
}