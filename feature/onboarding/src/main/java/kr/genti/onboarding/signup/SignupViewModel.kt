package kr.genti.onboarding.signup

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
import kr.genti.domain.entity.request.SignupRequestModel
import kr.genti.domain.entity.response.SignUpUserModel
import kr.genti.domain.enums.Gender
import kr.genti.domain.repository.InfoRepository
import kr.genti.domain.repository.UserRepository
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignupViewModel
@Inject
constructor(
    private val infoRepository: InfoRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _signupState = MutableStateFlow(SignupState())
    val signupState = _signupState.asStateFlow()

    private val _signupSideEffect = MutableSharedFlow<SignupSideEffect>()
    val signupSideEffect = _signupSideEffect.asSharedFlow()

    fun onIntent(intent: SignupIntent) {
        when (intent) {
            is SignupIntent.Init -> handleInit()
            is SignupIntent.GenderSelect -> handleGenderSelect(intent.gender)
            is SignupIntent.YearChange -> handleYearChange(intent.year)
            is SignupIntent.SignupBtnClick -> handleSignupBtnClick()
        }
    }

    private fun handleInit() {
        AmplitudeManager.trackEvent("view_infoget")
    }

    private fun handleGenderSelect(selectedGender: Gender) {
        Timber.tag("breeze").d("$selectedGender")
        _signupState.update {
            it.copy(
                selectedGender = selectedGender,
                isGenderSelected = true,
                isAllSelected = it.isYearSelected
            )
        }
    }

    private fun handleYearChange(selectedYear: String) {
        Timber.tag("breeze").d(selectedYear)
        _signupState.update {
            it.copy(
                selectedYear = selectedYear,
                isYearSelected = selectedYear.length == 4,
                isAllSelected = it.isGenderSelected && selectedYear.length == 4
            )
        }
    }

    private fun handleSignupBtnClick() {
        changeLoadingState(true)
        postSignupDataToServer()
    }

    private fun changeLoadingState(isLoading: Boolean) {
        _signupState.update { it.copy(isLoading = isLoading) }
    }

    private fun postSignupDataToServer() {
        viewModelScope.launch {
            infoRepository.postSignupData(
                SignupRequestModel(
                    signupState.value.selectedYear,
                    signupState.value.selectedGender.toString(),
                    null
                ),
            ).onSuccess { userData ->
                userRepository.setUserRole(ROLE_USER)
                setAmplitudeUserProperty(userData)
                _signupSideEffect.emit(SignupSideEffect.NavigateToTutorial)
            }.onFailure {
                _signupSideEffect.emit(SignupSideEffect.ShowErrorToast)
                changeLoadingState(false)
            }
        }
    }

    private fun setAmplitudeUserProperty(userData: SignUpUserModel) {
        AmplitudeManager.apply {
            trackEvent("complete_infoget")
            updateStringProperties("user_email", userData.email)
            updateStringProperties("user_platform", userData.lastLoginOauthPlatform)
            updateStringProperties("user_nickname", userData.nickname)
            updateStringProperties("user_birth_year", userData.birthYear)
            updateStringProperties("user_sex", userData.sex)
            updateIntProperties("user_share", 0)
            updateIntProperties("user_picturedownload", 0)
            updateIntProperties("user_main_scroll", 0)
            updateIntProperties("user_promptsuggest_refresh", 0)
            updateIntProperties("user_piccreate_total", 0)
            updateIntProperties("user_piccreate_original", 0)
            updateIntProperties("user_piccreate_oneparent", 0)
            updateIntProperties("user_piccreate_twoparents", 0)
            updateBooleanProperties("user_alarm", false)
            updateBooleanProperties("user_verified", false)
        }
    }

    companion object {
        private const val ROLE_USER = "USER"
    }
}