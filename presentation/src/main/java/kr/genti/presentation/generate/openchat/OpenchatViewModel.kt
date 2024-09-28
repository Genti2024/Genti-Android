package kr.genti.presentation.generate.openchat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.genti.core.state.UiState
import kr.genti.domain.entity.response.OpenchatModel
import kr.genti.domain.repository.GenerateRepository
import kr.genti.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class OpenchatViewModel
    @Inject
    constructor(
        private val generateRepository: GenerateRepository,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        private val _getOpenchatState = MutableStateFlow<UiState<OpenchatModel>>(UiState.Empty)
        val getOpenchatState: StateFlow<UiState<OpenchatModel>> = _getOpenchatState

        var isAccessible = true
        var isKakaoLaunched = false

        init {
            getOpenchatData()
        }

        private fun getOpenchatData() {
            _getOpenchatState.value = UiState.Loading
            viewModelScope.launch {
                generateRepository
                    .getOpenchatData()
                    .onSuccess {
                        userRepository.setIsChatAccessible(it.accessible)
                        _getOpenchatState.value = UiState.Success(it)
                    }.onFailure {
                        _getOpenchatState.value = UiState.Failure(it.message.toString())
                    }
            }
        }

        fun setIsChatAccessible() {
            userRepository.setIsChatAccessible(isAccessible)
        }
    }
