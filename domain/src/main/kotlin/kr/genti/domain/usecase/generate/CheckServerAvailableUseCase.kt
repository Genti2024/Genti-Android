package kr.genti.domain.usecase.generate

import kr.genti.domain.entity.response.ServerAvailableModel
import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

class CheckServerAvailableUseCase @Inject constructor(
    private val generateRepository: GenerateRepository,
) {
    suspend operator fun invoke(): Result<ServerAvailableModel> =
        runCatching {
            val response = generateRepository.getIsServerAvailable()
            return@runCatching response
        }
}