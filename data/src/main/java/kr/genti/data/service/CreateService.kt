package kr.genti.data.service

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.GenerateRequestDto
import kr.genti.data.dto.request.S3RequestDto
import kr.genti.data.dto.response.S3PresignedUrlDto
import retrofit2.http.Body
import retrofit2.http.POST

interface CreateService {
    @POST("/api/presigned-url")
    suspend fun getSingleS3Url(
        @Body request: S3RequestDto,
    ): BaseResponse<S3PresignedUrlDto>

    @POST("/api/presigned-url/many")
    suspend fun getMultiS3Url(
        @Body request: List<S3RequestDto>,
    ): BaseResponse<List<S3PresignedUrlDto>>

    @POST("/api/users/picture-generate-requests")
    suspend fun postToGenerate(
        @Body request: GenerateRequestDto,
    ): BaseResponse<Boolean>
}
