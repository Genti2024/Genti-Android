package kr.genti.domain.usecase.result

import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

class SubmitGenerateRatingUseCase @Inject constructor(
    private val generateRepository: GenerateRepository
) {
    suspend operator fun invoke(
        imageResponseId: Long,
        starRate: Int
    ): Result<Boolean> =
        runCatching {
            val isSuccess = generateRepository.postGenerateRate(
                responseId = imageResponseId.toInt(),
                star = starRate,
            )
            if (isSuccess) {
                return@runCatching true
            } else {
                throw Exception("submitting generate rating failed")
            }
        }
}