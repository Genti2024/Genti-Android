package kr.genti.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kr.genti.common.extension.noRippleClickable
import kr.genti.feature.main.R

@Composable
internal fun MainBottomBtn(
    modifier: Modifier = Modifier,
    visible: Boolean = true,
    onButtonClick: () -> Unit = {},
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, it.height) },
        modifier = modifier
            .navigationBarsPadding()
            .offset(y = 24.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.menu_create),
            contentDescription = null,
            modifier = Modifier.noRippleClickable { onButtonClick() }
        )
    }
}