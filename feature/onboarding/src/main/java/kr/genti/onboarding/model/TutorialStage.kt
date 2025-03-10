package kr.genti.onboarding.model

enum class TutorialStage {
    FIRST, SECOND, THIRD;

    companion object {
        fun getTutorialList() = entries
    }
}