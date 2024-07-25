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

        // TODO 수정
        val selectedYear = MutableLiveData<Int>()
        val isYearSelected = MutableLiveData<Boolean>(true)

        val isAllSelected = MutableLiveData<Boolean>(false)

        fun selectGender(gender: Gender) {
            selectedGender.value = gender
            isGenderSelected.value = true
            checkAllSelected()
        }

        fun checkAllSelected() {
            isAllSelected.value = isGenderSelected.value == true && isYearSelected.value == true
        }
    }
