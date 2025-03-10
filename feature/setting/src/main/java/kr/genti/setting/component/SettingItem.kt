package kr.genti.setting.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.White
import kr.genti.designsystem.theme.White60

@Composable
fun SettingItem(
    modifier: Modifier = Modifier,
    text: String = "",
    versionText: String = "",
    textColor: Color = White,
    isArrowVisible: Boolean = true,
    onItemClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
            .padding(horizontal = 16.dp)
            .noRippleClickable { onItemClick() }
    ) {
        Text(
            text = text,
            style = GentiTheme.typography.subtitle1,
            color = textColor,
            modifier = Modifier.align(Alignment.CenterStart)
        )

        if (versionText.isNotEmpty()) {
            Text(
                text = versionText,
                style = GentiTheme.typography.body2,
                color = White60,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }

        if (isArrowVisible) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_next),
                contentDescription = null,
                tint = White60,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Preview
@Composable
fun SettingItemPreview() {
    GentiTheme {
        Column {
            SettingItem(text = "이용 약관")
            SettingItem(text = "앱 버전 정보", isArrowVisible = false, versionText = "v1.0.0")
            SettingItem(text = "로그아웃", isArrowVisible = false, textColor = GentiGreen)
        }
    }
}