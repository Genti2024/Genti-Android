package kr.genti.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.genti.core.state.UiState
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.GenerateStatus
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.enums.PictureType
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

        init {
            getGenerateStatusFromServer()
        }

        private fun getGenerateStatusFromServer() {
            viewModelScope.launch {
                _getGenerateStatusState.value = UiState.Loading
                generateRepository.getGenerateStatus()
                    .onSuccess {
                        val isGenerating =
                            when (it.status) {
                                GenerateStatus.CREATED,
                                GenerateStatus.ASSIGNING,
                                GenerateStatus.IN_PROGRESS,
                                GenerateStatus.MATCH_TO_ADMIN,
                                -> true
                                else -> false
                            }
                        _getGenerateStatusState.value = UiState.Success(isGenerating)
                    }
                    .onFailure { t ->
                        _getGenerateStatusState.value = UiState.Failure(t.message.toString())
                    }
            }
        }

        val mockItemList =
            listOf(
                ImageModel(
                    0,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/0eb2d7f2-90d2-436a-aa53-4ad7a414d805",
                    "",
                    PictureRatio.RATIO_3_2,
                    PictureType.PictureCompleted,
                ),
                ImageModel(
                    0,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/68bf3348-f732-4874-947d-891f312b241e",
                    "",
                    PictureRatio.RATIO_3_2,
                    PictureType.PictureCompleted,
                ),
                ImageModel(
                    1,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/68bf3348-f732-4874-947d-891f312b241e",
                    "",
                    PictureRatio.RATIO_3_2,
                    PictureType.PictureCompleted,
                ),
                ImageModel(
                    2,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/0eb2d7f2-90d2-436a-aa53-4ad7a414d805",
                    "",
                    PictureRatio.RATIO_3_2,
                    PictureType.PictureCompleted,
                ),
                ImageModel(
                    3,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/0eb2d7f2-90d2-436a-aa53-4ad7a414d805",
                    "",
                    PictureRatio.RATIO_3_2,
                    PictureType.PictureCompleted,
                ),
                ImageModel(
                    4,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/68bf3348-f732-4874-947d-891f312b241e",
                    "",
                    PictureRatio.RATIO_3_2,
                    PictureType.PictureCompleted,
                ),
                ImageModel(
                    5,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/68bf3348-f732-4874-947d-891f312b241e",
                    "",
                    PictureRatio.RATIO_3_2,
                    PictureType.PictureCompleted,
                ),
                ImageModel(
                    6,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/68bf3348-f732-4874-947d-891f312b241e",
                    "",
                    PictureRatio.RATIO_3_2,
                    PictureType.PictureCompleted,
                ),
                ImageModel(
                    7,
                    "https://github.com/Genti2024/Genti-Android/assets/97405341/68bf3348-f732-4874-947d-891f312b241e",
                    "",
                    PictureRatio.RATIO_3_2,
                    PictureType.PictureCompleted,
                ),
            )
    }
