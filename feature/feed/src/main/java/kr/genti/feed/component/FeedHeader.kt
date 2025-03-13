package kr.genti.feed.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.White40

@Composable
internal fun FeedHeader(
    modifier: Modifier = Modifier,
    onInfoBtnClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Black)
            .padding(top = 20.dp)
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier
                    .width(77.dp)
                    .padding(vertical = 12.dp),
                painter = painterResource(id = R.drawable.logo_genti_2d),
                contentDescription = null
            )

            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .padding(4.dp)
                    .noRippleClickable { onInfoBtnClick() },
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_info),
                tint = White40,
                contentDescription = null
            )
        }

        Text(
            text = stringResource(R.string.feed_tv_title),
            style = GentiTheme.typography.subtitle1,
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.feed_tv_genfluencer_guide),
            style = GentiTheme.typography.body2,
            color = White40,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun FeedHeaderPreview() {
    GentiTheme {
        FeedHeader()
    }
}