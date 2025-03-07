package kr.genti.onboarding.signup

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val _signupState = MutableStateFlow(SignupState())
    val signupState = _signupState.asStateFlow()

    private val _signupSideEffect = MutableSharedFlow<SignupSideEffect>()
    val signupSideEffect = _signupSideEffect.asSharedFlow()

    fun onIntent(intent: SignupIntent) {
        when (intent) {
            is SignupIntent.Init -> handleInit()
            is SignupIntent.GenderSelect -> handleGenderSelect(intent.gender)
            is SignupIntent.YearInput -> handleYearInput(intent.year)
            is SignupIntent.SignupBtnClick -> handleSignupBtnClick()
        }
    }

    private fun handleInit() {

    }

    private fun handleGenderSelect(selectedGender: Gender) {

    }

    private fun handleYearInput(selectedYear: String) {

    }

    private fun handleSignupBtnClick() {

    }
}