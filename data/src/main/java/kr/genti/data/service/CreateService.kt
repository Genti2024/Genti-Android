package kr.genti.data.service

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.response.CreatePromptDto
import retrofit2.http.GET

interface CreateService {
    @GET("/api/user/examples/prompt-only")
    suspend fun getExamplePrompts(): BaseResponse<List<CreatePromptDto>>
}
