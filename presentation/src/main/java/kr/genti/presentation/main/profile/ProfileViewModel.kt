package kr.genti.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.genti.core.state.UiState
import kr.genti.domain.entity.response.PicturePagedListModel
import kr.genti.domain.enums.GenerateStatus
import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
    @Inject
    constructor(
        private val generateRepository: GenerateRepository,
    ) : ViewModel() {
        private val _getGenerateStatusState =
            MutableStateFlow<UiState<Boolean>>(UiState.Empty)
        val getGenerateStatusState: StateFlow<UiState<Boolean>> = _getGenerateStatusState

        private val _getPictureListState =
            MutableStateFlow<UiState<PicturePagedListModel>>(UiState.Loading)
        val getPictureListState: StateFlow<UiState<PicturePagedListModel>> = _getPictureListState

        private var currentPage = -1
        private var isPagingFinish = false
        private var totalPage = Int.MAX_VALUE

        var isFirstLoading = true

        fun getGenerateStatusFromServer() {
            viewModelScope.launch {
                _getGenerateStatusState.value = UiState.Loading
                generateRepository.getGenerateStatus()
                    .onSuccess {
                        _getGenerateStatusState.value =
                            UiState.Success(it.status == GenerateStatus.IN_PROGRESS)
                    }
                    .onFailure { t ->
                        _getGenerateStatusState.value = UiState.Failure(t.message.toString())
                    }
            }
        }

        fun getPictureListFromServer() {
            viewModelScope.launch {
                if (isPagingFinish) return@launch
                _getPictureListState.value = UiState.Loading
                generateRepository.getGeneratedPictureList(
                    ++currentPage,
                    10,
                    null,
                    null,
                )
                    .onSuccess {
                        totalPage = it.totalPages - 1
                        if (totalPage == currentPage) isPagingFinish = true
                        if (it.content.isEmpty()) {
                            _getPictureListState.value = UiState.Empty
                            return@launch
                        }
                        if (isFirstLoading) isFirstLoading = false
                        _getPictureListState.value = UiState.Success(it)
                    }.onFailure {
                        _getPictureListState.value = UiState.Failure(it.message.toString())
                    }
            }
        }
    }
