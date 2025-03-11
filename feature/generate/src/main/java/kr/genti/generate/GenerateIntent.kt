package kr.genti.generate

sealed class GenerateIntent {
    data object Init : GenerateIntent()
    data class PromptChange(val year: String) : GenerateIntent()
    data class TextFieldFocused(val isFocused: Boolean) : GenerateIntent()
    data object BackBtnClick : GenerateIntent()
    data object NextBtnClick : GenerateIntent()
}