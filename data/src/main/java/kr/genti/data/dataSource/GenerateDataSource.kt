package kr.genti.data.dataSource

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.response.GenerateStatusDto
import kr.genti.data.dto.response.PicturePagedListDto

interface GenerateDataSource {
    suspend fun getGenerateStatus(): BaseResponse<GenerateStatusDto>

    suspend fun getGeneratedPictureList(
        page: Int,
        size: Int,
        sortBy: String?,
        direction: String?,
    ): BaseResponse<PicturePagedListDto>
}
