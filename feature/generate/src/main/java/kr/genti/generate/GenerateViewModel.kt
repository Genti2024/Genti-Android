package kr.genti.generate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
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
import kr.genti.generate.model.GenerateType.Companion.getGenerateType
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
            is GenerateIntent.PromptExampleSwipe -> handlePromptExampleSwipe()
            is GenerateIntent.PromptChange -> handlePromptChange(intent.prompt)
            is GenerateIntent.TextFieldFocused -> handleTextFieldOutsideClick(intent.isFocused)
            is GenerateIntent.RatioSelect -> handleRatioSelect(intent.pictureRatio)
            is GenerateIntent.ImageSelectBtnClick -> handleImageSelectBtnClick()
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
        viewModelScope.launch {
            getExamplePrompt()
        }
    }

    private fun handleBackBtnClick() {
        val prevStage =
            generateState.value.currentStage.prevStage(generateState.value.isParentPic)
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
        val nextStage =
            generateState.value.currentStage.nextStage(generateState.value.pictureNumber)
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

    private fun handlePromptExampleSwipe() {
        // TODO : 앰플리튜드 작업
    }

    private fun handlePromptChange(prompt: String) {
        _generateState.update {
            it.copy(prompt = prompt)
        }
    }

    private fun handleTextFieldOutsideClick(isFocusClearNeeded: Boolean) {
        _generateState.update {
            it.copy(isFocusClearNeeded = isFocusClearNeeded)
        }
    }

    private fun handleRatioSelect(pictureRatio: PictureRatio) {
        _generateState.update {
            it.copy(pictureRatio = pictureRatio)
        }
    }

    private fun handleImageSelectBtnClick() {

    }

    private suspend fun getExamplePrompt() {
        createRepository.getPromptExample(
            getGenerateType(
                generateState.value.isParentPic,
                generateState.value.pictureNumber
            ).name
        ).onSuccess { result ->
            _generateState.update {
                it.copy(exampleList = result.toImmutableList())
            }
        }
    }

    private suspend fun sendImagesToGenerate() {

    }
}