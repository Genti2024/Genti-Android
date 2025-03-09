package kr.genti.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.White40

@Composable
internal fun ProfileTopBar(
    modifier: Modifier = Modifier,
    onSettingBtnClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.profile_tv_title),
            style = GentiTheme.typography.title,
            modifier = Modifier.padding(start = 16.dp)
        )
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_setting),
            contentDescription = null,
            tint = White40,
            modifier = Modifier
                .size(48.dp)
                .padding(8.dp)
                .padding(end = 10.dp)
                .noRippleClickable { onSettingBtnClick() },
        )
    }
}