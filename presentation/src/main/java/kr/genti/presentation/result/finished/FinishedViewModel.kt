package kr.genti.presentation.result.finished

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kr.genti.domain.entity.request.ReportRequestModel
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.repository.GenerateRepository
import kr.genti.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class FinishedViewModel
    @Inject
    constructor(
        private val generateRepository: GenerateRepository,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        val errorReport = MutableLiveData<String>()
        val isWritten = MutableLiveData(false)

        var finishedImage =
            ImageModel(
                -1,
                "",
                "",
                null,
                null,
            )

        var isRatioGaro = true

        private val _postReportResult = MutableSharedFlow<Boolean>()
        val postReportResult: SharedFlow<Boolean> = _postReportResult

        private val _postRateResult = MutableSharedFlow<Boolean>()
        val postRateResult: SharedFlow<Boolean> = _postRateResult

        private val _postVerifyResult = MutableSharedFlow<Boolean>()
        val postVerifyResult: SharedFlow<Boolean> = _postVerifyResult

        fun checkWritten() {
            isWritten.value = errorReport.value?.isNotEmpty()
        }

        fun setPictureRatio() {
            isRatioGaro = finishedImage.pictureRatio == PictureRatio.RATIO_GARO
        }

        fun postGenerateRateToServer(star: Int) {
            viewModelScope.launch {
                generateRepository.postGenerateRate(
                    finishedImage.id.toInt(),
                    star,
                )
                    .onSuccess {
                        _postRateResult.emit(true)
                    }
                    .onFailure {
                        _postRateResult.emit(false)
                    }
            }
        }

        fun postGenerateReportToServer() {
            viewModelScope.launch {
                generateRepository.postGenerateReport(
                    ReportRequestModel(
                        finishedImage.id,
                        errorReport.value.orEmpty(),
                    ),
                )
                    .onSuccess {
                        _postReportResult.emit(true)
                    }
                    .onFailure {
                        _postReportResult.emit(false)
                    }
            }
        }

        fun postVerifyGenerateStateToServer() {
            viewModelScope.launch {
                generateRepository.postVerifyGenerateState(finishedImage.id.toInt())
                    .onSuccess {
                        _postVerifyResult.emit(true)
                    }
                    .onFailure {
                        _postVerifyResult.emit(false)
                    }
            }
        }

        fun getIsOpenchatAccessible() = userRepository.getIsChatAccessible()
    }
