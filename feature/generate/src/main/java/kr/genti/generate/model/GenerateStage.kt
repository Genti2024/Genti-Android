package kr.genti.generate.model

import kr.genti.domain.enums.PictureNumber

enum class GenerateStage {
    INIT, NUMBER_SELECT, PROMPT_INPUT, RATIO_SELECT, IMAGE_THREE_SELECT, IMAGE_SIX_SELECT, RESULT;

    fun prevStage(isParentPic: Boolean): GenerateStage = when (this) {
        NUMBER_SELECT -> INIT
        PROMPT_INPUT -> if (isParentPic) NUMBER_SELECT else INIT
        RATIO_SELECT -> PROMPT_INPUT
        IMAGE_THREE_SELECT -> RATIO_SELECT
        IMAGE_SIX_SELECT -> RATIO_SELECT
        else -> RESULT
    }

    fun nextStage(pictureNumber: PictureNumber): GenerateStage = when (this) {
        NUMBER_SELECT -> PROMPT_INPUT
        PROMPT_INPUT -> RATIO_SELECT
        RATIO_SELECT -> if (pictureNumber != PictureNumber.TWO) IMAGE_THREE_SELECT else IMAGE_SIX_SELECT
        else -> RESULT
    }
}