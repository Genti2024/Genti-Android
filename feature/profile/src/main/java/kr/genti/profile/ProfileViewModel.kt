package kr.genti.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.genti.common.manager.AmplitudeManager
import kr.genti.common.manager.ImageManager
import kr.genti.common.manager.ImageManager.checkExternalStoragePermission
import kr.genti.common.manager.ImageManager.saveImageToStorage
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.GenerateStatus
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.usecase.profile.GetGeneratedPictureListUseCase
import kr.genti.domain.usecase.result.GetCurrentGenerateStatusUseCase
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val getGeneratedPictureListUseCase: GetGeneratedPictureListUseCase,
    private val getCurrentGenerateStatusUseCase: GetCurrentGenerateStatusUseCase
) : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()

    private val _profileSideEffect = MutableSharedFlow<ProfileSideEffect>()
    val profileSideEffect = _profileSideEffect.asSharedFlow()

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.Init -> handleInit()
            is ProfileIntent.Refresh -> handleRefresh()
            is ProfileIntent.ImageItemClick -> handleImageItemClick(intent.item)
            is ProfileIntent.GenerateBtnClick -> handleGenerateBtnClick()
            is ProfileIntent.SettingBtnClick -> handleSettingBtnClick()
            is ProfileIntent.LastColumnLoaded -> handleLastColumnLoaded()
            is ProfileIntent.SaveBtnClick -> handleSaveBtnClick()
            is ProfileIntent.ShareBtnClick -> handleShareBtnClick()
            is ProfileIntent.DialogDismiss -> handleDialogDismiss()
        }
    }

    private fun handleInit() {
        viewModelScope.launch {
            changeLoadingState(true)
            getGenerateStatusFromServer()
            getPictureListFromServer()
            changeLoadingState(false)
        }
    }

    private fun handleRefresh() {
        viewModelScope.launch {
            _profileState.update { it.copy(isRefreshing = true) }
            getGenerateStatusFromServer()
            getPictureListFromServer()
            delay(500)
            _profileState.update { it.copy(isRefreshing = false) }
        }
    }

    private fun handleImageItemClick(item: ImageModel) {
        _profileState.update {
            it.copy(
                detailImageId = item.id,
                detailImageUrl = item.url,
                isDetailImageGaro = item.pictureRatio == PictureRatio.RATIO_GARO,
                isDetailDialogShown = true
            )
        }
    }

    private fun handleGenerateBtnClick() {
        AmplitudeManager.trackEvent("click_createpictab")
        viewModelScope.launch {
            _profileSideEffect.emit(ProfileSideEffect.NavigateToGenerate)
        }
    }

    private fun handleSettingBtnClick() {
        viewModelScope.launch {
            _profileSideEffect.emit(ProfileSideEffect.NavigateToSetting)
        }
    }

    private fun handleLastColumnLoaded() {
        viewModelScope.launch {
            getPictureListFromServer()
        }
    }

    private fun handleSaveBtnClick() {
        viewModelScope.launch {
            if (!checkExternalStoragePermission()) {
                _profileSideEffect.emit(ProfileSideEffect.StartPermissionLauncher)
                return@launch
            }
            saveImageToStorage(
                id = profileState.value.detailImageId,
                imageUrl = profileState.value.detailImageUrl,
            ).onSuccess {
                updateAmplitude("picdownload", "user_picturedownload")
                _profileSideEffect.emit(ProfileSideEffect.ShowDownloadToast)
            }.onFailure {
                _profileSideEffect.emit(ProfileSideEffect.ShowErrorToast)
            }
        }
    }

    private fun handleShareBtnClick() {
        viewModelScope.launch {
            ImageManager.getCacheImageUri(
                id = profileState.value.detailImageId,
                imageUrl = profileState.value.detailImageUrl,
            ).onSuccess { uri ->
                updateAmplitude("picshare", "user_share")
                _profileSideEffect.emit(ProfileSideEffect.NavigateToShare(uri))
            }.onFailure {
                _profileSideEffect.emit(ProfileSideEffect.ShowErrorToast)
            }
        }
    }

    private fun handleDialogDismiss() {
        _profileState.update {
            it.copy(isDetailDialogShown = false)
        }
    }

    private fun changeLoadingState(isLoading: Boolean) {
        _profileState.update {
            it.copy(isLoading = isLoading)
        }
    }

    private suspend fun getGenerateStatusFromServer() {
        getCurrentGenerateStatusUseCase()
            .onSuccess { result ->
                _profileState.update {
                    it.copy(isGenerating = result.status == GenerateStatus.IN_PROGRESS)
                }
            }.onFailure {
                _profileSideEffect.emit(ProfileSideEffect.ShowErrorToast)
            }
    }

    private suspend fun getPictureListFromServer() {
        if (profileState.value.isPagingFinish) return
        val nextPage = profileState.value.currentPage + 1
        getGeneratedPictureListUseCase(nextPage)
            .onSuccess { result ->
                _profileState.update {
                    it.copy(
                        totalPage = result.totalPages,
                        currentPage = nextPage,
                        isPagingFinish = result.totalPages == nextPage,
                        itemList = (it.itemList + result.content).toImmutableList(),
                    )
                }
            }.onFailure {
                _profileSideEffect.emit(ProfileSideEffect.ShowErrorToast)
            }
    }

    private fun updateAmplitude(event: String, property: String) {
        AmplitudeManager.apply {
            trackEvent(
                EVENT_CLICK_BTN,
                mapOf(PROPERTY_PAGE to "mypage"),
                mapOf(PROPERTY_BTN to event),
            )
            plusIntProperties(property)
        }
    }
}