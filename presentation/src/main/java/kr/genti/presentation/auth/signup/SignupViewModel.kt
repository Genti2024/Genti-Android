package kr.genti.presentation.auth.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kr.genti.domain.enums.Gender
import javax.inject.Inject

@HiltViewModel
class SignupViewModel
    @Inject
    constructor(
        // private val authRepository: AuthRepository,
    ) : ViewModel() {
        val selectedGender = MutableLiveData<Gender>(Gender.NONE)
        val isGenderSelected = MutableLiveData<Boolean>(false)

        val selectedYear = MutableLiveData<Int>()
        val selectedYearText = MutableLiveData<String>()
        val isYearSelected = MutableLiveData<Boolean>(false)

        val isAllSelected = MutableLiveData<Boolean>(false)

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
    }
