package kr.genti.presentation.generate.verify

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.genti.core.state.UiState
import kr.genti.domain.entity.request.KeyRequestModel
import kr.genti.domain.entity.request.S3RequestModel
import kr.genti.domain.entity.response.ImageFileModel.Companion.emptyImageFileModel
import kr.genti.domain.entity.response.S3PresignedUrlModel
import kr.genti.domain.enums.FileType
import kr.genti.domain.repository.CreateRepository
import kr.genti.domain.repository.UploadRepository
import kr.genti.presentation.BuildConfig
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel
    @Inject
    constructor(
        private val createRepository: CreateRepository,
        private val uploadRepository: UploadRepository,
    ) : ViewModel() {
        var userImage = emptyImageFileModel()
        private var imageS3Key = KeyRequestModel(null)

        private val _totalGeneratingState = MutableStateFlow<UiState<Boolean>>(UiState.Empty)
        val totalGeneratingState: StateFlow<UiState<Boolean>> = _totalGeneratingState

        fun getSingleS3Url() {
            _totalGeneratingState.value = UiState.Loading
            viewModelScope.launch {
                createRepository
                    .getS3SingleUrl(
                        S3RequestModel(
                            if (BuildConfig.DEBUG) FileType.DEV_USER_VERIFICATION_IMAGE else FileType.USER_VERIFICATION_IMAGE,
                            userImage.name,
                        ),
                    ).onSuccess { uriModel ->
                        imageS3Key = KeyRequestModel(uriModel.s3Key)
                        postSingleImage(uriModel)
                    }.onFailure {
                        _totalGeneratingState.value = UiState.Failure(it.message.toString())
                    }
            }
        }

        private fun postSingleImage(urlModel: S3PresignedUrlModel) {
            viewModelScope.launch {
                uploadRepository
                    .uploadImage(urlModel.url, userImage.url)
                    .onSuccess {
                        postToVerifyImage()
                    }.onFailure {
                        _totalGeneratingState.value = UiState.Failure(it.message.toString())
                    }
            }
        }

        private fun postToVerifyImage() {
        }
    }
