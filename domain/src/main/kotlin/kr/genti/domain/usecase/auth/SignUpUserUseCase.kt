package kr.genti.domain.usecase.auth

import kr.genti.domain.entity.request.SignupRequestModel
import kr.genti.domain.entity.response.SignUpUserModel
import kr.genti.domain.enums.Gender
import kr.genti.domain.repository.InfoRepository
import kr.genti.domain.repository.UserRepository
import javax.inject.Inject

class SignUpUserUseCase @Inject constructor(
    private val infoRepository: InfoRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        birthYear: String,
        gender: Gender,
        phoneNumber: String? = null
    ): Result<SignUpUserModel> =
        runCatching {
            val request = SignupRequestModel(
                birthYear,
                gender.toString(),
                phoneNumber
            )
            val response = infoRepository.postSignupData(request)
            userRepository.setUserRole(ROLE_USER)
            return@runCatching response
        }

    companion object {
        private const val ROLE_USER = "USER"
    }
}