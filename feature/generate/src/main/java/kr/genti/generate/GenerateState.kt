package kr.genti.generate

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.genti.domain.entity.response.PromptExampleModel
import kr.genti.domain.enums.Gender
import kr.genti.domain.enums.PictureNumber
import kr.genti.domain.enums.PictureRatio
import kr.genti.generate.model.GenerateStage

data class GenerateState(
    val currentStage: GenerateStage = GenerateStage.INIT,
    val currentStep: Int = 0,
    val isParentPic: Boolean = false,
    val pictureNumber: PictureNumber = PictureNumber.NONE,
    val exampleList: ImmutableList<PromptExampleModel> = persistentListOf(),
    val prompt: String = "",
    val isFocusClearNeeded: Boolean = false,
    val pictureRatio: PictureRatio = PictureRatio.NONE,
    val selectedGender: Gender = Gender.NONE,
) {
    val progress: Float
        get() = currentStep / if (!isParentPic) 3F else 4F
}