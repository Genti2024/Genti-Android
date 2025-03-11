package kr.genti.generate

import kr.genti.domain.enums.Gender
import kr.genti.domain.enums.PictureRatio
import kr.genti.generate.model.GenerateStage

data class GenerateState(
    val currentStage: GenerateStage = GenerateStage.PROMPT_INPUT,
    val currentStep: Int = 1,
    val isParentPic: Boolean = false,
    val isGroupImage: Boolean = false,
    val prompt: String = "",
    val selectedRatio: PictureRatio = PictureRatio.NONE,
    val selectedGender: Gender = Gender.NONE,
)