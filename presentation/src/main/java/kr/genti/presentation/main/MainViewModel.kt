package kr.genti.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kr.genti.domain.entity.response.GenerateStatusModel
import kr.genti.domain.enums.GenerateStatus
import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val generateRepository: GenerateRepository,
    ) : ViewModel() {
        private val _getStatusResult = MutableSharedFlow<Boolean>()
        val getStatusResult: SharedFlow<Boolean> = _getStatusResult

        var currentStatus: GenerateStatus = GenerateStatus.COMPLETED
        lateinit var newPicture: GenerateStatusModel

        init {
            getGenerateStatusFromServer()
        }

        private fun getGenerateStatusFromServer() {
            viewModelScope.launch {
                generateRepository.getGenerateStatus()
                    .onSuccess {
                        currentStatus = GenerateStatus.COMPLETED
                        newPicture = it
                    }
                    .onFailure {
                        _getStatusResult.emit(false)
                    }
            }
        }

        fun checkNerPictureInitialized() = ::newPicture.isInitialized
    }
