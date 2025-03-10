package kr.genti.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kr.genti.core.designsystem.R
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiGreen
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.designsystem.theme.White60
import kr.genti.onboarding.model.TutorialStage

@Composable
fun TutorialItem(
    modifier: Modifier = Modifier,
    currentStage: TutorialStage = TutorialStage.FIRST,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(100.dp))

        Text(
            text = stringResource(R.string.onboarding_tv_title),
            style = GentiTheme.typography.body1,
            color = GentiGreen
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(
                when (currentStage) {
                    TutorialStage.FIRST -> R.string.onboarding_tv_first_subtitle
                    TutorialStage.SECOND -> R.string.onboarding_tv_second_subtitle
                    TutorialStage.THIRD -> R.string.onboarding_tv_third_subtitle
                }
            ),
            style = GentiTheme.typography.title,
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(
                when (currentStage) {
                    TutorialStage.FIRST -> R.string.onboarding_tv_first_body
                    TutorialStage.SECOND -> R.string.onboarding_tv_second_body
                    TutorialStage.THIRD -> R.string.onboarding_tv_third_body
                }
            ),
            style = GentiTheme.typography.body2,
            color = White60
        )

        Spacer(modifier = Modifier.height(36.dp))

        if (currentStage != TutorialStage.THIRD) {
            Image(
                painter = painterResource(
                    if (currentStage == TutorialStage.FIRST) {
                        R.drawable.img_onboarding_first
                    } else {
                        R.drawable.img_onboarding_second
                    }
                ),
                contentDescription = null
            )
        }
    }
}

@Preview(backgroundColor = 0xFF030F0F, showBackground = true)
@Composable
fun TutorialItemPreview() {
    GentiTheme {
        TutorialItem()
    }
}