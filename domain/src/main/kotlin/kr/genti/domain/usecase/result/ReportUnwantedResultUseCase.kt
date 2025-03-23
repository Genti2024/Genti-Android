package kr.genti.domain.usecase.result

import kr.genti.domain.entity.request.ReportRequestModel
import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

class ReportUnwantedResultUseCase @Inject constructor(
    private val generateRepository: GenerateRepository
) {
    suspend operator fun invoke(
        imageResponseId: Long,
        reportText: String
    ): Result<Boolean> =
        runCatching {
            val request = ReportRequestModel(
                pictureGenerateResponseId = imageResponseId,
                content = reportText
            )
            val isSuccess = generateRepository.postGenerateReport(request)
            if (isSuccess) {
                return@runCatching true
            } else {
                throw Exception("reporting generate result failed")
            }
        }
}