package kr.genti.onboarding.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kr.genti.core.designsystem.R
import kr.genti.onboarding.model.TutorialStage

@Composable
fun TutorialFadeInBackground(
    modifier: Modifier = Modifier,
    currentStage: TutorialStage = TutorialStage.FIRST,
) {
    AnimatedVisibility(
        visible = currentStage == TutorialStage.THIRD,
        enter = fadeIn(animationSpec = tween(durationMillis = 500)),
        exit = fadeOut(),
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_onboarding_third),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth()
        )
    }
}