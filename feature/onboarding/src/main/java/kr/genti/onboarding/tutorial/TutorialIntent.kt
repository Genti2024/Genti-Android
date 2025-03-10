package kr.genti.onboarding.tutorial

import kr.genti.onboarding.model.TutorialStage

sealed class TutorialIntent {
    data object NextBtnClick: TutorialIntent()
}