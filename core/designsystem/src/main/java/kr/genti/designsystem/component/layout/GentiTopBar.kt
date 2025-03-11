package kr.genti.designsystem.component.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.White

@Composable
fun GentiTopBar(
    modifier: Modifier = Modifier,
    titleText: String = "",
    isGenerate: Boolean = false,
    currentStep: Int = 1,
    totalStep: Int = 3,
    onBackButtonClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_back),
            contentDescription = null,
            tint = White,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(10.dp)
                .noRippleClickable { onBackButtonClick() }
                .padding(start = 6.dp)
                .padding(top = 3.dp)
        )

        Text(
            text = titleText,
            style = GentiTheme.typography.subtitle1,
            modifier = Modifier.align(Alignment.Center),
        )

        if (isGenerate) {
            Text(
                text = "$currentStep/$totalStep",
                style = GentiTheme.typography.subtitle1,
                color = GentiGreen,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
            )
        }
    }
}

@Preview
@Composable
fun GentiTopBarPreview() {
    GentiTheme {
        GentiTopBar(
            titleText = "젠티",
        )
    }
}