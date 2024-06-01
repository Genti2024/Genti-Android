package kr.genti.data.dataSource

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.response.CreatePromptDto

interface CreateDataSource {
    suspend fun getExamplePrompts(): BaseResponse<List<CreatePromptDto>>
}
