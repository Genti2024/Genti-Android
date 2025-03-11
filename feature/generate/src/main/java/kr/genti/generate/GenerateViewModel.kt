package kr.genti.generate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.genti.domain.enums.PictureNumber
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.repository.CreateRepository
import kr.genti.domain.repository.UploadRepository
import kr.genti.generate.model.GenerateStage
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
            is GenerateIntent.BackBtnClick -> handleBackBtnClick()
            is GenerateIntent.NextBtnClick -> handleNextBtnClick()
            is GenerateIntent.NumberSelect -> handleNumberSelect(intent.pictureNumber)
            is GenerateIntent.PromptChange -> handlePromptChange()
            is GenerateIntent.TextFieldFocused -> handleTextFieldOutsideClick(intent.isFocused)
            is GenerateIntent.RatioSelect -> handleRatioSelect(intent.pictureRatio)
        }
    }

    private fun handleInit(isParentPic: Boolean) {
        _generateState.update {
            it.copy(
                isParentPic = isParentPic,
                currentStep = 1,
                currentStage = if (isParentPic) GenerateStage.NUMBER_SELECT else GenerateStage.PROMPT_INPUT
            )
        }
    }

    private fun handleBackBtnClick() {
        val prevStage = generateState.value.currentStage.prevStage(generateState.value.isParentPic)
        if (prevStage != GenerateStage.INIT) {
            _generateState.update {
                it.copy(currentStage = prevStage, currentStep = it.currentStep - 1)
            }
        } else {
            viewModelScope.launch {
                _generateSideEffect.emit(GenerateSideEffect.NavigateToBack)
            }
        }
    }

    private fun handleNextBtnClick() {
        val nextStage = generateState.value.currentStage.nextStage()
        if (nextStage != GenerateStage.RESULT) {
            _generateState.update {
                it.copy(currentStage = nextStage, currentStep = it.currentStep + 1)
            }
        } else {
            viewModelScope.launch {
                sendImagesToGenerate()
            }
        }
    }

    private fun handleNumberSelect(pictureNumber: PictureNumber) {
        _generateState.update {
            it.copy(pictureNumber = pictureNumber)
        }
    }

    private fun handlePromptChange() {

    }

    private fun handleTextFieldOutsideClick(isFocused: Boolean) {

    }

    private fun handleRatioSelect(pictureRatio: PictureRatio) {
        _generateState.update {
            it.copy(pictureRatio = pictureRatio)
        }
    }

    private suspend fun sendImagesToGenerate() {

    }
}