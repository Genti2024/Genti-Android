package kr.genti.main

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kr.genti.domain.repository.GenerateRepository
import kr.genti.main.navigation.MainTab
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val generateRepository: GenerateRepository,
) : ViewModel() {
    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()

    private val _mainSideEffect = MutableSharedFlow<MainSideEffect>()
    val mainSideEffect = _mainSideEffect.asSharedFlow()

    fun onIntent(intent: MainIntent) {
        when (intent) {
            is MainIntent.Init -> handleInit()
            is MainIntent.TabSelect -> handleTabClick(intent.tab)
            is MainIntent.GenerateBtnClick -> handleGenerateBtnClick()
        }
    }

    private fun handleInit() {

    }

    private fun handleTabClick(tab: MainTab) {

    }

    private fun handleGenerateBtnClick() {

    }

    private suspend fun getGenerateStatus() {
        generateRepository.getGenerateStatus()
            .onSuccess { result ->
                _mainState.update {
                    it.copy(
                        currentStatus = result.status,
                        generatedImage = result
                    )
                }
            }
    }
}