package kr.genti.designsystem.component.button

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.Disabled
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.GrayBtn

@Composable
fun GentiButton(
    modifier: Modifier = Modifier,
    @StringRes textRes: Int = R.string.app_name,
    @DrawableRes iconRes: Int? = null,
    btnColor: Color = GentiGreen,
    textColor: Color = GrayBtn,
    isActive: Boolean = true,
    onClick: () -> Unit = {}
) {
    val containerColor = if (isActive) btnColor else Disabled
    val contentColor = if (isActive) textColor else Black

    Card(
        modifier = modifier
            .fillMaxWidth()
            .noRippleClickable { if (isActive) onClick() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp)
        ) {
            if (iconRes != null) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = iconRes),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }
            Text(
                text = stringResource(textRes),
                style = GentiTheme.typography.subtitle2,
                color = contentColor,
            )
        }
    }
}

@Preview
@Composable
fun GentiButtonsPreview() {
    GentiTheme {
        Column {
            GentiButton(
                textRes = R.string.btn_download,
                iconRes = R.drawable.ic_download_no_border,
                isActive = false,
            )

            Spacer(modifier = Modifier.height(16.dp))

            GentiButton(
                textRes = R.string.btn_download,
                iconRes = R.drawable.ic_download_no_border,
                isActive = true,
            )
        }
    }
}