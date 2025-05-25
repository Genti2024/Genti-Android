package kr.genti.result.verify

import android.net.Uri
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
import kr.genti.common.manager.AmplitudeManager.EVENT_CLICK_BTN
import kr.genti.common.manager.AmplitudeManager.PROPERTY_BTN
import kr.genti.common.manager.AmplitudeManager.PROPERTY_PAGE
import kr.genti.common.manager.AmplitudeManager.updateBooleanProperties
import kr.genti.common.manager.ImageManager
import kr.genti.core.common.BuildConfig
import kr.genti.domain.entity.response.ImageBucketModel
import kr.genti.domain.usecase.upload.UploadImageToBucketUseCase
import kr.genti.domain.usecase.verify.CheckVerifyImageUploadedUseCase
import kr.genti.domain.usecase.verify.GetVerifyImageBucketUseCase
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel
@Inject
constructor(
    private val getVerifyImageBucketUseCase: GetVerifyImageBucketUseCase,
    private val uploadImageToBucketUseCase: UploadImageToBucketUseCase,
    private val checkVerifyImageUploadedUseCase: CheckVerifyImageUploadedUseCase
) : ViewModel() {

    private val _verifyState = MutableStateFlow(VerifyState())
    val verifyState = _verifyState.asStateFlow()

    private val _verifySideEffect = MutableSharedFlow<VerifySideEffect>()
    val verifySideEffect = _verifySideEffect.asSharedFlow()

    fun onIntent(intent: VerifyIntent) {
        when (intent) {
            is VerifyIntent.CameraButtonClick -> handleCameraButtonClick(intent.isFirst)
            is VerifyIntent.CameraPermissionGrant -> handleCameraPermissionGrant()
            is VerifyIntent.CameraResultSuccess -> handleCameraResultSuccess()
            is VerifyIntent.FinishButtonClick -> handleFinishButtonClick()
            is VerifyIntent.BackButtonClick -> handleExitDialog(true)
            is VerifyIntent.ExitButtonClick -> handleExitButtonClick()
            is VerifyIntent.ExitDialogDismiss -> handleExitDialog(false)
        }
    }

    private fun handleCameraButtonClick(isFirst: Boolean) {
        trackAmplitude(if (isFirst) "verifyme" else "photoretake")
        viewModelScope.launch {
            _verifySideEffect.emit(VerifySideEffect.StartPermissionLauncher)
        }
    }

    private fun handleCameraPermissionGrant() {
        viewModelScope.launch {
            makeTempFileAndStartCamera()
        }
    }

    private fun handleCameraResultSuccess() {
        _verifyState.update {
            it.copy(isPhotoTaken = true)
        }
    }

    private fun handleFinishButtonClick() {
        trackAmplitude("verifymedone")
        viewModelScope.launch {
            changeLoadingState(true)
            sendVerifyImageToServer()
            changeLoadingState(false)
        }
    }

    private fun handleExitButtonClick() {
        trackAmplitude("exit")
        viewModelScope.launch {
            _verifySideEffect.emit(VerifySideEffect.NavigateToBack)
        }
    }

    private fun handleExitDialog(isVisible: Boolean) {
        _verifyState.update {
            it.copy(isExitDialogVisible = isVisible)
        }
    }

    private fun changeLoadingState(isLoading: Boolean) {
        _verifyState.update {
            it.copy(isLoading = isLoading)
        }
    }

    private suspend fun makeTempFileAndStartCamera() {
        ImageManager.getTempImageFile()
            .onSuccess { file ->
                _verifyState.update {
                    it.copy(
                        imageUri = Uri.fromFile(file),
                        imageName = file.name,
                        isPhotoTaken = false
                    )
                }
                _verifySideEffect.emit(VerifySideEffect.StartCameraLauncher)
            }.onFailure {
                _verifySideEffect.emit(VerifySideEffect.ShowErrorToast)
            }
    }

    private suspend fun sendVerifyImageToServer() {
        runCatching {
            val imageBucket = getSingleImageBucket()
            uploadImageToBucket(imageBucket.presignedUrl)
            postToVerifyImage(imageBucket.s3Key)
        }.onSuccess {
            updateBooleanProperties("user_verified", true)
            _verifySideEffect.emit(VerifySideEffect.VerifySuccess)
        }.onFailure {
            _verifySideEffect.emit(VerifySideEffect.ShowErrorToast)
        }
    }

    private suspend fun getSingleImageBucket(): ImageBucketModel =
        getVerifyImageBucketUseCase(
            imageName = verifyState.value.imageName,
            isDebugMode = BuildConfig.DEBUG
        ).getOrThrow()

    private suspend fun uploadImageToBucket(presignedUrl: String) {
        uploadImageToBucketUseCase(
            bucketUrl = presignedUrl,
            imageUrl = verifyState.value.imageUri.toString()
        ).getOrThrow()
    }

    private suspend fun postToVerifyImage(s3Key: String) {
        checkVerifyImageUploadedUseCase(
            bucketKey = s3Key
        ).getOrThrow()
    }

    private fun trackAmplitude(btnName: String) {
        val pageName = if (verifyState.value.isPhotoTaken) "verifyme2" else "verifyme1"
        AmplitudeManager.trackEvent(
            EVENT_CLICK_BTN,
            mapOf(PROPERTY_PAGE to pageName),
            mapOf(PROPERTY_BTN to btnName),
        )
    }
}