package kr.genti.generate.model

enum class GenerateStage {
    INIT, NUMBER_SELECT, PROMPT_INPUT, RATIO_SELECT, IMAGE_SELECT, RESULT;

    fun prevStage(isParentPic: Boolean): GenerateStage = when (this) {
        NUMBER_SELECT -> INIT
        PROMPT_INPUT -> if (isParentPic) NUMBER_SELECT else INIT
        RATIO_SELECT -> PROMPT_INPUT
        IMAGE_SELECT -> RATIO_SELECT
        else -> RESULT
    }

    fun nextStage(): GenerateStage = when (this) {
        NUMBER_SELECT -> PROMPT_INPUT
        PROMPT_INPUT -> RATIO_SELECT
        RATIO_SELECT -> IMAGE_SELECT
        else -> RESULT
    }
}