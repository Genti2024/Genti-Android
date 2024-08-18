package kr.genti.presentation.auth.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.genti.core.state.UiState
import kr.genti.domain.entity.request.SignupRequestModel
import kr.genti.domain.entity.response.SignUpUserModel
import kr.genti.domain.enums.Gender
import kr.genti.domain.repository.InfoRepository
import kr.genti.domain.repository.UserRepository
import javax.inject.Inject

@HiltViewModel
class SignupViewModel
    @Inject
    constructor(
        private val infoRepository: InfoRepository,
        private val userRepository: UserRepository,
    ) : ViewModel() {
        val selectedGender = MutableLiveData<Gender>(Gender.NONE)
        val isGenderSelected = MutableLiveData<Boolean>(false)

        val selectedYear = MutableLiveData<Int>()
        val selectedYearText = MutableLiveData<String>()
        val isYearSelected = MutableLiveData<Boolean>(false)

        val isAllSelected = MutableLiveData<Boolean>(false)

        private val _postSignupState = MutableStateFlow<UiState<SignUpUserModel>>(UiState.Empty)
        val postSignupState: StateFlow<UiState<SignUpUserModel>> = _postSignupState

        fun selectGender(gender: Gender) {
            selectedGender.value = gender
            isGenderSelected.value = true
            checkAllSelected()
        }

        fun selectBirthYear(year: Int) {
            selectedYear.value = year
            if (isYearSelected.value == true) selectedYearText.value = year.toString() + "년"
        }

        fun showYearPicker() {
            isYearSelected.value = true
            selectedYearText.value = selectedYear.value.toString() + "년"
            checkAllSelected()
        }

        private fun checkAllSelected() {
            isAllSelected.value = isGenderSelected.value == true && isYearSelected.value == true
        }

        fun postSignupDataToServer() {
            _postSignupState.value = UiState.Loading
            viewModelScope.launch {
                infoRepository.postSignupData(
                    SignupRequestModel(
                        selectedYear.value.toString(),
                        selectedGender.value.toString(),
                    ),
                ).onSuccess {
                    userRepository.setUserRole(ROLE_USER)
                    _postSignupState.value = UiState.Success(it)
                }.onFailure {
                    _postSignupState.value = UiState.Failure(it.message.orEmpty())
                }
            }
        }

        companion object {
            private const val ROLE_USER = "USER"
        }
    }
