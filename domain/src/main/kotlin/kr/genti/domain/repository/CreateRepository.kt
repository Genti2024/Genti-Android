package kr.genti.domain.repository

import kr.genti.domain.entity.request.GenerateRequestModel
import kr.genti.domain.entity.request.S3RequestModel
import kr.genti.domain.entity.response.S3PresignedUrlModel

interface CreateRepository {
    suspend fun getS3SingleUrl(request: S3RequestModel): Result<S3PresignedUrlModel>

    suspend fun getS3MultiUrl(request: List<S3RequestModel>): Result<List<S3PresignedUrlModel>>

    suspend fun postToGenerate(request: GenerateRequestModel): Result<Boolean>
}
