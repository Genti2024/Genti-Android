package kr.genti.main.component

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

@Composable
fun GenerateSelectItem(
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int,
    @StringRes bodyRes: Int,
    @StringRes captionRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(Gray, RoundedCornerShape(6.dp))
            .fillMaxWidth()
            .padding(20.dp)
            .noRippleClickable { onClick() }
    ) {
        Text(
            text = stringResource(titleRes),
            style = GentiTheme.typography.subtitle2
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(subtitleRes),
            style = GentiTheme.typography.body1,
            color = GentiGreen
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(bodyRes),
            style = GentiTheme.typography.caption2
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(captionRes),
            style = GentiTheme.typography.caption2,
            color = White60
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GenerateSelectItemPreview() {
    GentiTheme {
        GenerateSelectItem(
            titleRes = R.string.create_select_tv_default_title,
            subtitleRes = R.string.create_select_tv_default_subtitle,
            bodyRes = R.string.create_select_tv_default_body,
            captionRes = R.string.create_select_tv_default_caption,
        )
    }
}