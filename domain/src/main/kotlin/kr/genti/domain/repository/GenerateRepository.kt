package kr.genti.domain.repository

import kr.genti.domain.entity.response.GenerateStatusModel
import kr.genti.domain.entity.response.PicturePagedListModel

interface GenerateRepository {
    suspend fun getGenerateStatus(): Result<GenerateStatusModel>

    suspend fun getGeneratedPictureList(
        page: Int,
        size: Int,
        sortBy: String?,
        direction: String?,
    ): Result<PicturePagedListModel>
}
