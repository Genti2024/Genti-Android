package kr.genti.generate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.Gray
import kr.genti.designsystem.theme.White60
import kr.genti.domain.entity.response.ImageFileModel

@Composable
internal fun ImagePersonItem(
    modifier: Modifier = Modifier,
    title: String = "",
    imageList: List<ImageFileModel> = listOf(),
    onBtnClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(color = Gray, shape = RoundedCornerShape(6.dp))
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = GentiTheme.typography.body1,
                    modifier = Modifier.padding(start = 12.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = stringResource(if (imageList.size != 3) R.string.selfie_tv_btn_select else R.string.selfie_tv_btn_reselect),
                    style = GentiTheme.typography.body2,
                    color = if (imageList.size != 3) GentiGreen else White60,
                    modifier = Modifier
                        .padding(12.dp)
                        .noRippleClickable { onBtnClick() }
                )
            }

            ImageThreeSelected(
                imageList = imageList,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .padding(top = 2.dp, bottom = 12.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ImagePersonItem() {
    GentiTheme {
        ImagePersonItem(
            title = "첫번째 인물",
            imageList = listOf()
        )
    }
}