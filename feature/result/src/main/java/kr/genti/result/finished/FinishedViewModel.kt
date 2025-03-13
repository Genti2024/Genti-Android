package kr.genti.result.finished

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.genti.common.manager.AmplitudeManager
import kr.genti.common.manager.AmplitudeManager.PROPERTY_TYPE
import kr.genti.common.manager.ImageManager
import kr.genti.common.manager.ImageManager.checkExternalStoragePermission
import kr.genti.common.manager.ImageManager.saveImageToStorage
import kr.genti.domain.entity.request.ReportRequestModel
import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

@HiltViewModel
class FinishedViewModel
@Inject
constructor(
    private val generateRepository: GenerateRepository
) : ViewModel() {
    private val _finishedState = MutableStateFlow(FinishedState())
    val finishedState = _finishedState.asStateFlow()

    private val _finishedSideEffect = MutableSharedFlow<FinishedSideEffect>()
    val finishedSideEffect = _finishedSideEffect.asSharedFlow()

    fun onIntent(intent: FinishedIntent) {
        when (intent) {
            is FinishedIntent.Init -> handleInit(intent)
            is FinishedIntent.ImageClick -> handleImageClick()
            is FinishedIntent.BackButtonClick -> handleBackButtonClick()
            is FinishedIntent.ReportButtonClick -> handleReportButtonClick()
            is FinishedIntent.DialogDismiss -> handleDialogDismiss()
            is FinishedIntent.ShareButtonClick -> handleShareButtonClick()
            is FinishedIntent.DownloadButtonClick -> handleDownloadButtonClick()
            is FinishedIntent.ReportTextChange -> handleReportTextChange(intent.text)
            is FinishedIntent.ReportSubmitButtonClick -> handleReportSubmitButtonClick()
            is FinishedIntent.FinishButtonClick -> handleFinishButtonClick()
            is FinishedIntent.RatingChange -> handleRatingChange(intent.rating)
            is FinishedIntent.RatingSubmitButtonClick -> handleRatingSubmitButtonClick()
            is FinishedIntent.RatingSkipButtonClick -> handleRatingSkipButtonClick()
        }
    }

    private fun handleInit(intent: FinishedIntent.Init) {
        _finishedState.update {
            it.copy(
                responseId = intent.responseId,
                isParentPic = intent.isParentPic,
                imageUrl = intent.imageUrl
            )
        }
    }

    private fun handleImageClick() {
        amplitudeTrackEvent("enlarge_picdone_picture")
        _finishedState.update {
            it.copy(isDetailDialogVisible = true)
        }
    }

    private fun handleBackButtonClick() {
        amplitudeTrackEvent("gomain")
        _finishedState.update {
            it.copy(isRatingDialogVisible = true)
        }
    }

    private fun handleReportButtonClick() {
        _finishedState.update {
            it.copy(isReportDialogVisible = true)
        }
    }

    private fun handleDialogDismiss() {
        _finishedState.update {
            it.copy(
                isDetailDialogVisible = false,
                isReportDialogVisible = false,
                isRatingDialogVisible = false
            )
        }
    }

    private fun handleShareButtonClick() {
        viewModelScope.launch {
            ImageManager.getCacheImageUri(
                id = finishedState.value.responseId,
                imageUrl = finishedState.value.imageUrl,
            ).onSuccess { uri ->
                amplitudeTrackShare()
                _finishedSideEffect.emit(FinishedSideEffect.NavigateToShare(uri))
            }.onFailure {
                _finishedSideEffect.emit(FinishedSideEffect.ShowErrorToast)
            }
        }
    }

    private fun handleDownloadButtonClick() {
        viewModelScope.launch {
            if (!checkExternalStoragePermission()) {
                _finishedSideEffect.emit(FinishedSideEffect.StartPermissionLauncher)
                return@launch
            }
            saveImageToStorage(
                id = finishedState.value.responseId,
                imageUrl = finishedState.value.imageUrl,
            ).onSuccess {
                amplitudeTrackDownload()
                _finishedSideEffect.emit(FinishedSideEffect.ShowDownloadToast)
            }.onFailure {
                _finishedSideEffect.emit(FinishedSideEffect.ShowErrorToast)
            }
        }
    }

    private fun handleReportTextChange(text: String) {
        _finishedState.update {
            it.copy(reportText = text)
        }
    }

    private fun handleReportSubmitButtonClick() {
        viewModelScope.launch {
            generateRepository.postGenerateReport(
                ReportRequestModel(
                    finishedState.value.responseId,
                    finishedState.value.reportText,
                ),
            ).onSuccess {
                _finishedState.update {
                    it.copy(isReportSubmitted = true)
                }
            }.onFailure {
                _finishedSideEffect.emit(FinishedSideEffect.ShowErrorToast)
            }
        }
    }

    private fun handleFinishButtonClick() {
        amplitudeTrackEvent("reportpic_picdone")
        viewModelScope.launch {
            _finishedSideEffect.emit(FinishedSideEffect.NavigateToBack)
        }
    }

    private fun handleRatingChange(rating: Int) {
        _finishedState.update {
            it.copy(rating = rating)
        }
    }

    private fun handleRatingSubmitButtonClick() {
        viewModelScope.launch {
            generateRepository.postGenerateRate(
                responseId = finishedState.value.responseId.toInt(),
                star = finishedState.value.rating,
            ).onSuccess {
                amplitudeTrackEvent("ratingsubmit_picdone")
                _finishedSideEffect.emit(FinishedSideEffect.NavigateToBack)
            }.onFailure {
                _finishedSideEffect.emit(FinishedSideEffect.ShowErrorToast)
            }
        }
    }

    private fun handleRatingSkipButtonClick() {
        viewModelScope.launch {
            generateRepository.postVerifyGenerateState(finishedState.value.responseId.toInt())
                .onSuccess {
                    amplitudeTrackEvent("ratingpass_picdone")
                    _finishedSideEffect.emit(FinishedSideEffect.NavigateToBack)
                }.onFailure {
                    _finishedSideEffect.emit(FinishedSideEffect.ShowErrorToast)
                }
        }
    }

    private fun amplitudeTrackEvent(event: String) {
        AmplitudeManager.trackEvent(
            event,
            mapOf(PROPERTY_TYPE to if (finishedState.value.isParentPic) "parents" else "original")
        )
    }

    private fun amplitudeTrackDownload() {
        AmplitudeManager.apply {
            trackEvent(
                EVENT_CLICK_BTN,
                mapOf(PROPERTY_TYPE to if (finishedState.value.isParentPic) "parents" else "original"),
                mapOf(PROPERTY_BTN to "picdownload")
            )
            plusIntProperties("user_picturedownload")
        }
    }

    private fun amplitudeTrackShare() {
        AmplitudeManager.apply {
            trackEvent(
                EVENT_CLICK_BTN,
                mapOf(PROPERTY_TYPE to if (finishedState.value.isParentPic) "parents" else "original"),
                mapOf(PROPERTY_BTN to "picshare")
            )
            plusIntProperties("user_share")
        }
    }
}