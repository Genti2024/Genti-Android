package kr.genti.domain.usecase.result

import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

class ForceGenerateInDebugUseCase @Inject constructor(
    private val generateRepository: GenerateRepository
) {
    suspend operator fun invoke(): Result<Boolean> =
        runCatching {
            val isSuccess = generateRepository.patchStatusInDevelop()
            if (isSuccess) {
                return@runCatching true
            } else {
                throw Exception("force generate in debug failed")
            }
        }
}