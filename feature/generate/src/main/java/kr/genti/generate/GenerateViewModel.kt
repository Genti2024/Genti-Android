package kr.genti.generate

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kr.genti.domain.repository.CreateRepository
import kr.genti.domain.repository.UploadRepository
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel
@Inject
constructor(
    private val createRepository: CreateRepository,
    private val uploadRepository: UploadRepository,
) : ViewModel() {
    private val _generateState = MutableStateFlow(GenerateState())
    val generateState = _generateState.asStateFlow()

    private val _generateSideEffect = MutableSharedFlow<GenerateSideEffect>()
    val generateSideEffect = _generateSideEffect.asSharedFlow()

    fun onIntent(intent: GenerateIntent) {
        when (intent) {
            is GenerateIntent.Init -> handleInit(intent.isParentPic)
            is GenerateIntent.PromptChange -> handlePromptChange()
            is GenerateIntent.TextFieldFocused -> handleTextFieldOutsideClick(intent.isFocused)
            is GenerateIntent.BackBtnClick -> handleBackBtnClick()
            is GenerateIntent.NextBtnClick -> handleNextBtnClick()
        }
    }

    private fun handleInit(isParentPic: Boolean) {
        _generateState.update {
            it.copy(isParentPic = isParentPic)
        }
    }

    private fun handlePromptChange() {

    }

    private fun handleTextFieldOutsideClick(isFocused: Boolean) {

    }

    private fun handleBackBtnClick() {

    }

    private fun handleNextBtnClick() {

    }
}