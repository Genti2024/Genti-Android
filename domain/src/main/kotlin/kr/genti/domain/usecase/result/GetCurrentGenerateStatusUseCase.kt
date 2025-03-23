package kr.genti.domain.usecase.result

import kr.genti.domain.entity.response.GenerateStatusModel
import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

class GetCurrentGenerateStatusUseCase @Inject constructor(
    private val generateRepository: GenerateRepository,
) {
    suspend operator fun invoke(): Result<GenerateStatusModel> =
        runCatching {
            val response = generateRepository.getGenerateStatus()
            return@runCatching response
        }
}