package kr.genti.onboarding.signup

import kr.genti.domain.enums.Gender

data class SignupState(
    val selectedGender: Gender = Gender.NONE,
    val selectedYear: String = "",
    val isGenderSelected: Boolean = false,
    val isYearSelected: Boolean = false,
    val isAllSelected: Boolean = false,
    val isLoading: Boolean = false,
)