package kr.genti.data.dataSource

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.GenerateRequestDto
import kr.genti.data.dto.request.S3RequestDto
import kr.genti.data.dto.response.CreatePromptDto
import kr.genti.data.dto.response.S3PresignedUrlDto

interface CreateDataSource {
    suspend fun getExamplePrompts(): BaseResponse<List<CreatePromptDto>>

    suspend fun getSingleS3Url(request: S3RequestDto): BaseResponse<S3PresignedUrlDto>

    suspend fun getMultiS3Url(request: List<S3RequestDto>): BaseResponse<List<S3PresignedUrlDto>>

    suspend fun postToGenerate(request: GenerateRequestDto): BaseResponse<Boolean>
}
