package kr.genti.domain.usecase.setting

import kr.genti.domain.repository.InfoRepository
import kr.genti.domain.repository.UserRepository
import javax.inject.Inject

class DeleteUserUseCase @Inject constructor(
    private val infoRepository: InfoRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Result<Boolean> =
        runCatching {
            val isSuccess = infoRepository.deleteUser()
            userRepository.clearInfo()
            if (isSuccess) {
                return@runCatching true
            } else {
                throw Exception("delete failed")
            }
        }
}