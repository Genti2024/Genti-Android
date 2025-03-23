package kr.genti.domain.usecase.generate

import kr.genti.domain.entity.response.PromptExampleModel
import kr.genti.domain.repository.CreateRepository
import javax.inject.Inject

class GetPromptExampleListUseCase @Inject constructor(
    private val createRepository: CreateRepository,
) {
    suspend operator fun invoke(
        generateType: String,
    ): Result<List<PromptExampleModel>> =
        runCatching {
            val response = createRepository.getPromptExample(generateType)
            return@runCatching response
        }
}