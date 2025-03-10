package kr.genti.onboarding.tutorial

import kr.genti.onboarding.model.TutorialStage

data class TutorialState(
    val currentStage: TutorialStage = TutorialStage.FIRST,
)