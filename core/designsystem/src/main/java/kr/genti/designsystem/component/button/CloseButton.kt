package kr.genti.designsystem.component.button

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.White

@Composable
fun BoxScope.CloseButton(
    modifier: Modifier = Modifier,
    onCloseBtnClicked: () -> Unit = {}
) {
    Icon(
        imageVector = ImageVector.vectorResource(id = R.drawable.ic_close),
        contentDescription = null,
        tint = White,
        modifier = modifier
            .padding(16.dp)
            .padding(top = 16.dp)
            .align(Alignment.TopEnd)
            .noRippleClickable { onCloseBtnClicked() }
    )
}