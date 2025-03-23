package kr.genti.domain.usecase.auth

import kr.genti.domain.entity.request.ReissueRequestModel
import kr.genti.domain.entity.response.ReissueTokenModel
import kr.genti.domain.repository.AuthRepository
import kr.genti.domain.repository.UserRepository
import javax.inject.Inject

class ReissueOldTokensUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<ReissueTokenModel> =
        runCatching {
            val response = authRepository.postReissueTokens(
                ReissueRequestModel(
                    userRepository.getAccessToken(),
                    userRepository.getRefreshToken()
                )
            )
            userRepository.setTokens(
                response.accessToken,
                response.refreshToken
            )
            return@runCatching response
        }
}