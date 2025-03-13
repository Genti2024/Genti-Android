package kr.genti.designsystem.component.item

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.Disabled
import kr.genti.designsystem.theme.GentiGreen

@Composable
fun GentiRatingBar(
    rating: Int = 5,
    onRatingChanged: (Int) -> Unit = {},
) {
    Row {
        for (index in 1..5) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_star),
                contentDescription = null,
                tint = if (index <= rating) GentiGreen else Disabled,
                modifier = Modifier.noRippleClickable { onRatingChanged(index) }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewGentiRatingBar() {
    GentiRatingBar(
        rating = 3
    )
}