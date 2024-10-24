package kr.genti.domain.repository

import kr.genti.domain.entity.request.CreateRequestModel
import kr.genti.domain.entity.request.KeyRequestModel
import kr.genti.domain.entity.request.S3RequestModel
import kr.genti.domain.entity.response.S3PresignedUrlModel

interface CreateRepository {
    suspend fun getS3SingleUrl(request: S3RequestModel): Result<S3PresignedUrlModel>

    suspend fun getS3MultiUrl(request: List<S3RequestModel>): Result<List<S3PresignedUrlModel>>

    suspend fun postToCreate(request: CreateRequestModel): Result<Boolean>

    suspend fun postToVerify(request: KeyRequestModel): Result<Boolean>
}
