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
import javax.inject.Inject

@HiltViewModel
class FinishedViewModel
    @Inject
    constructor(
        private val generateRepository: GenerateRepository,
    ) : ViewModel() {
        val errorReport = MutableLiveData<String>()

        var finishedImage =
            ImageModel(
                -1,
                "",
                "",
                null,
                null,
            )

        var isRatio23 = true

        private val _postReportResult = MutableSharedFlow<Boolean>()
        val postReportResult: SharedFlow<Boolean> = _postReportResult

        fun setPictureRatio() {
            isRatio23 = finishedImage.pictureRatio?.name == PictureRatio.RATIO_2_3.name
        }

        fun postGenerateReport() {
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
    }
