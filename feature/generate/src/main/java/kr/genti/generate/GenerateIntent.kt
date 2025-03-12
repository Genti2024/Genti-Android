package kr.genti.generate

import kr.genti.domain.enums.PictureNumber
import kr.genti.domain.enums.PictureRatio

sealed class GenerateIntent {
    data class Init(val isParentPic: Boolean) : GenerateIntent()
    data object BackBtnClick : GenerateIntent()
    data object NextBtnClick : GenerateIntent()
    data class NumberSelect(val pictureNumber: PictureNumber) : GenerateIntent()
    data object PromptExampleSwipe : GenerateIntent()
    data class PromptChange(val prompt: String) : GenerateIntent()
    data class RatioSelect(val pictureRatio: PictureRatio) : GenerateIntent()
    data class TextFieldFocused(val isFocused: Boolean) : GenerateIntent()
    data object ImageSelectBtnClick : GenerateIntent()
}