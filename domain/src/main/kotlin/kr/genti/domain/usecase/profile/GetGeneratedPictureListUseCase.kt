package kr.genti.domain.usecase.profile

import kr.genti.domain.entity.response.PicturePagedListModel
import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

class GetGeneratedPictureListUseCase @Inject constructor(
    private val generateRepository: GenerateRepository
) {
    suspend operator fun invoke(
        page: Int,
        size: Int = 10
    ): Result<PicturePagedListModel> =
        runCatching {
            val response = generateRepository.getGeneratedPictureList(
                page = page,
                size = size
            )
            return@runCatching response
        }
}