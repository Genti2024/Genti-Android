package kr.genti.domain.usecase.result

import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

class ResetGenerateStatusUseCase @Inject constructor(
    private val generateRepository: GenerateRepository
) {
    suspend operator fun invoke(
        generateRequestId: Long?
    ): Result<Boolean> =
        runCatching {
            val isSuccess = generateRepository.getCanceledToReset(
                requestId = requireNotNull(generateRequestId) { "generateRequestId is null" }.toString()
            )
            if (isSuccess) {
                return@runCatching true
            } else {
                throw Exception("resetting generate status failed")
            }
        }
}