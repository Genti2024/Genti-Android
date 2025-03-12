package kr.genti.generate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.component.button.GentiGradationIconButton
import kr.genti.designsystem.component.item.GentiCheckedText
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Gray
import kr.genti.designsystem.theme.Transparent
import kr.genti.designsystem.theme.White40
import kr.genti.domain.entity.response.PromptExampleModel
import kr.genti.generate.component.PromptExamplePager

@Composable
fun PromptInputScreen(
    modifier: Modifier = Modifier,
    isParentPic: Boolean = false,
    exampleList: ImmutableList<PromptExampleModel> = persistentListOf(),
    prompt: String = "",
    onExamplePageSwipe: () -> Unit = {},
    onPromptChanged: (String) -> Unit = {},
    onTextFieldOutsideClicked: () -> Unit = {},
    onNextBtnClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .noRippleClickable { onTextFieldOutsideClicked() }
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
                .padding(bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.create_tv_script_title),
                style = GentiTheme.typography.title,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            GentiCheckedText(text = stringResource(R.string.create_tv_script_subtitle_1))

            Spacer(modifier = Modifier.height(4.dp))

            if (isParentPic) {
                GentiCheckedText(text = stringResource(R.string.create_tv_script_subtitle_2_parent))
            } else {
                GentiCheckedText(text = stringResource(R.string.create_tv_script_subtitle_2))
            }

            if (exampleList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.create_tv_random_title),
                    style = GentiTheme.typography.body1,
                    color = GentiGreen,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                PromptExamplePager(
                    exampleList = exampleList,
                    onExamplePageSwipe = onExamplePageSwipe
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    color = Gray,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Black, shape = RoundedCornerShape(10.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                BasicTextField(
                    value = prompt,
                    onValueChange = { newValue -> onPromptChanged(newValue) },
                    textStyle = GentiTheme.typography.body2,
                    cursorBrush = SolidColor(Transparent),
                    decorationBox = { innerTextField ->
                        Box {
                            if (prompt.isEmpty()) {
                                Text(
                                    text = stringResource(id = R.string.create_et_script_hint),
                                    style = GentiTheme.typography.body2,
                                    color = White40
                                )
                            }
                            innerTextField()
                        }
                    },
                    modifier = Modifier
                        .weight(1F)
                        .align(Alignment.CenterVertically)
                        .padding(8.dp)
                )

                GentiGradationIconButton(
                    iconRes = R.drawable.ic_play,
                    isActivate = prompt.isNotEmpty(),
                    onBtnClick = onNextBtnClick,
                    modifier = Modifier
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF030F0F)
@Composable
private fun PromptInputScreenPreview() {
    GentiTheme {
        PromptInputScreen(
            exampleList = persistentListOf(
                PromptExampleModel(
                    url = "",
                    prompt = stringResource(R.string.create_tv_example_1)
                ),
                PromptExampleModel(
                    url = "",
                    prompt = stringResource(R.string.create_tv_example_1)
                ),
                PromptExampleModel(
                    url = "",
                    prompt = stringResource(R.string.create_tv_example_1)
                )
            )
        )
    }
}