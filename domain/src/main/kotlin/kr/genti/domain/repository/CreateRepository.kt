package kr.genti.domain.repository

import kr.genti.domain.entity.request.S3RequestModel
import kr.genti.domain.entity.response.PromptModel
import kr.genti.domain.entity.response.S3PresignedUrlModel

interface CreateRepository {
    suspend fun getExamplePrompts(): Result<List<PromptModel>>

    suspend fun getS3SingleUrl(request: S3RequestModel): Result<S3PresignedUrlModel>
}
