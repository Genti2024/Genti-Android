package kr.genti.domain.usecase.setting

import kr.genti.domain.repository.InfoRepository
import kr.genti.domain.repository.UserRepository
import javax.inject.Inject

class LogoutUserUseCase @Inject constructor(
    private val infoRepository: InfoRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Boolean> =
        runCatching {
            val isSuccess = infoRepository.postUserLogout()
            userRepository.clearInfo()
            if (isSuccess) {
                return@runCatching true
            } else {
                throw Exception("logout failed")
            }
        }
}