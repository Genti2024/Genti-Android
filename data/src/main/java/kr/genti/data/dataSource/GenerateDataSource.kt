package kr.genti.data.dataSource

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.response.GenerateStatusDto

interface GenerateDataSource {
    suspend fun getGenerateStatus(): BaseResponse<GenerateStatusDto>
}
