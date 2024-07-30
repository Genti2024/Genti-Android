package kr.genti.presentation.auth.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kr.genti.domain.entity.request.SignupRequestModel
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

        private val _postSignupResult = MutableSharedFlow<Boolean>()
        val postSignupResult: SharedFlow<Boolean> = _postSignupResult

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

        fun checkAllSelected() {
            isAllSelected.value = isGenderSelected.value == true && isYearSelected.value == true
        }

        fun postSignupDataToServer() {
            viewModelScope.launch {
                infoRepository.postSignupData(
                    SignupRequestModel(
                        selectedYear.value.toString(),
                        selectedGender.value.toString(),
                    ),
                ).onSuccess {
                    _postSignupResult.emit(it)
                    userRepository.setUserRole(ROLE_USER)
                }.onFailure {
                    _postSignupResult.emit(false)
                }
            }
        }

        companion object {
            private const val ROLE_USER = "USER"
        }
    }
