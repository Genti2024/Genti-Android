package kr.genti.domain.usecase.verify

import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

class CheckUserVerifiedUseCase @Inject constructor(
    private val generateRepository: GenerateRepository,
) {
    suspend operator fun invoke(): Result<Boolean> =
        runCatching {
            val isVerified = generateRepository.getIsUserVerified()
            return@runCatching isVerified
        }
}