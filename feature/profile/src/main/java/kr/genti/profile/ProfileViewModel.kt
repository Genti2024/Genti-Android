package kr.genti.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val generateRepository: GenerateRepository,
) : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState = _profileState.asStateFlow()

    private val _profileSideEffect = MutableSharedFlow<ProfileSideEffect>()
    val profileSideEffect = _profileSideEffect.asSharedFlow()

    fun onIntent(intent: ProfileIntent) {
        when (intent) {
            is ProfileIntent.Init -> handleInit()
            is ProfileIntent.ImageItemClick -> handleImageItemClick()
            is ProfileIntent.GenerateBtnClick -> handleGenerateBtnClick()
            is ProfileIntent.SettingBtnClick -> handleSettingBtnClick()
        }
    }

    private fun handleInit() {

    }

    private fun handleImageItemClick() {

    }

    private fun handleGenerateBtnClick() {

    }

    private fun handleSettingBtnClick() {
        
    }
}