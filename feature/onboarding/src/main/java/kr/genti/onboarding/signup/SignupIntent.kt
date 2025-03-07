package kr.genti.onboarding.signup

import kr.genti.domain.enums.Gender

sealed class SignupIntent {
    data object Init : SignupIntent()
    data class GenderSelect(val gender: Gender) : SignupIntent()
    data class YearChange(val year: String) : SignupIntent()
    data object SignupBtnClick : SignupIntent()
}