package kr.genti.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kr.genti.common.extension.noRippleClickable
import kr.genti.core.designsystem.R

@Composable
internal fun GenerateForceButton(
    modifier: Modifier = Modifier,
    isVisible: Boolean = false,
    onDebugPatchBtnClicked: () -> Unit = {}
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideIn { IntOffset(0, it.height) },
        exit = fadeOut() + slideOut { IntOffset(0, it.height) },
        modifier = modifier
            .noRippleClickable { onDebugPatchBtnClicked() }
            .navigationBarsPadding()
            .padding(bottom = 90.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_download),
            contentDescription = null,
        )
    }
}