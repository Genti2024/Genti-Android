package kr.genti.data.service

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.response.GenerateStatusDto
import retrofit2.http.GET

interface GenerateService {
    @GET("/api/users/picture-generate-requests/pending")
    suspend fun getGenerateStatus(): BaseResponse<GenerateStatusDto>
}
