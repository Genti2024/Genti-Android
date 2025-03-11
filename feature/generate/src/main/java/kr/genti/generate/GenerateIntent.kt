package kr.genti.generate

import kr.genti.domain.enums.PictureNumber

sealed class GenerateIntent {
    data class Init(val isParentPic: Boolean) : GenerateIntent()
    data object BackBtnClick : GenerateIntent()
    data object NextBtnClick : GenerateIntent()
    data class NumberSelect(val pictureNumber: PictureNumber) : GenerateIntent()
    data class PromptChange(val year: String) : GenerateIntent()
    data class TextFieldFocused(val isFocused: Boolean) : GenerateIntent()
}