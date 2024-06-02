package kr.genti.data.service

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.S3RequestDto
import kr.genti.data.dto.response.CreatePromptDto
import kr.genti.data.dto.response.S3PresignedUrlDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CreateService {
    @GET("/api/user/examples/prompt-only")
    suspend fun getExamplePrompts(): BaseResponse<List<CreatePromptDto>>

    @POST("/api/presigned-url")
    suspend fun getSingleS3Url(
        @Body request: S3RequestDto,
    ): BaseResponse<S3PresignedUrlDto>

    @POST("/api/presigned-url/many")
    suspend fun getMultiS3Url(
        @Body request: List<S3RequestDto>,
    ): BaseResponse<List<S3PresignedUrlDto>>
}
