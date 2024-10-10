package kr.genti.presentation.generate.finished

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kr.genti.domain.entity.request.ReportRequestModel
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.repository.GenerateRepository
import kr.genti.domain.repository.UserRepository
import java.io.File
import java.io.FileOutputStream
import java.net.URL
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

        private val _isImageDownloaded = MutableSharedFlow<Boolean>()
        val isImageDownloaded: SharedFlow<Boolean> = _isImageDownloaded

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

        fun downloadImageToCache(outputFile: File) {
            viewModelScope.launch {
                runCatching {
                    withContext(Dispatchers.IO) {
                        val connection = URL(finishedImage.url).openConnection()
                        connection.connect()
                        connection.getInputStream()?.use { inputStream ->
                            FileOutputStream(outputFile).use { out ->
                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, out)
                            }
                        }
                    }
                }.onSuccess {
                    _isImageDownloaded.emit(true)
                }.onFailure {
                    _isImageDownloaded.emit(false)
                }
            }
        }

        fun postGenerateRateToServer(star: Int) {
            viewModelScope.launch {
                generateRepository
                    .postGenerateRate(
                        finishedImage.id.toInt(),
                        star,
                    ).onSuccess {
                        _postRateResult.emit(true)
                    }.onFailure {
                        _postRateResult.emit(false)
                    }
            }
        }

        fun postGenerateReportToServer() {
            viewModelScope.launch {
                generateRepository
                    .postGenerateReport(
                        ReportRequestModel(
                            finishedImage.id,
                            errorReport.value.orEmpty(),
                        ),
                    ).onSuccess {
                        _postReportResult.emit(true)
                    }.onFailure {
                        _postReportResult.emit(false)
                    }
            }
        }

        fun postVerifyGenerateStateToServer() {
            viewModelScope.launch {
                generateRepository
                    .postVerifyGenerateState(finishedImage.id.toInt())
                    .onSuccess {
                        _postVerifyResult.emit(true)
                    }.onFailure {
                        _postVerifyResult.emit(false)
                    }
            }
        }

        fun getIsOpenchatAccessible() = userRepository.getIsChatAccessible()
    }
