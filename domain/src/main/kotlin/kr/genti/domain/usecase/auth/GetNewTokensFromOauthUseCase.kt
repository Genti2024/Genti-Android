package kr.genti.domain.usecase.auth

import kr.genti.domain.entity.request.AuthRequestModel
import kr.genti.domain.repository.AuthRepository
import kr.genti.domain.repository.UserRepository
import javax.inject.Inject

class GetNewTokensFromOauthUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        newAccessToken: String,
        fcmToken: String
    ): Result<Boolean> =
        runCatching {
            val request = AuthRequestModel(newAccessToken, fcmToken)
            val response = authRepository.postOauthDataToGetToken(request)
            with(userRepository) {
                setTokens(response.accessToken, response.refreshToken)
                setUserRole(response.userRoleString)
            }
            val isAssigned = response.userRoleString == ALREADY_ASSIGNED
            return@runCatching isAssigned
        }

    companion object {
        const val ALREADY_ASSIGNED = "USER"
    }
}