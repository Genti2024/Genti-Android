package kr.genti.presentation.result.waiting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

@HiltViewModel
class WaitingViewModel
    @Inject
    constructor(
        private val generateRepository: GenerateRepository,
    ) : ViewModel() {
        private val _postResetResult = MutableSharedFlow<Boolean>()
        val postResetResult: SharedFlow<Boolean> = _postResetResult

        fun postResetStateToServer(id: Int) {
            viewModelScope.launch {
                generateRepository.postResetState(id)
                    .onSuccess {
                        _postResetResult.emit(true)
                    }
                    .onFailure {
                        _postResetResult.emit(false)
                    }
            }
        }
    }
