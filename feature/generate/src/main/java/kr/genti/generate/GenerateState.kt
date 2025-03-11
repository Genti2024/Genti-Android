package kr.genti.generate

import kr.genti.domain.enums.Gender
import kr.genti.domain.enums.PictureNumber
import kr.genti.domain.enums.PictureRatio
import kr.genti.generate.model.GenerateStage

data class GenerateState(
    val currentStage: GenerateStage = GenerateStage.PROMPT_INPUT,
    val currentStep: Int = 0,
    val isParentPic: Boolean = false,
    val pictureNumber: PictureNumber = PictureNumber.NONE,
    val prompt: String = "",
    val selectedRatio: PictureRatio = PictureRatio.NONE,
    val selectedGender: Gender = Gender.NONE,
) {
    val progress: Float
        get() = currentStep / if (!isParentPic) 3F else 4F
}