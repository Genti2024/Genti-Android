package kr.genti.domain.usecase.result

import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

class SkipGenerateRatingUseCase @Inject constructor(
    private val generateRepository: GenerateRepository
) {
    suspend operator fun invoke(
        imageResponseId: Long,
    ): Result<Boolean> =
        runCatching {
            val isSuccess = generateRepository.postVerifyGenerateState(
                responseId = imageResponseId.toInt()
            )
            if (isSuccess) {
                return@runCatching true
            } else {
                throw Exception("skipping generate rating failed")
            }
        }
}