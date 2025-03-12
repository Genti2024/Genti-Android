package kr.genti.domain.repository

import kr.genti.domain.entity.request.CreateRequestModel
import kr.genti.domain.entity.request.CreateTwoRequestModel
import kr.genti.domain.entity.request.KeyRequestModel
import kr.genti.domain.entity.request.PurchaseValidRequestModel
import kr.genti.domain.entity.request.ImageBucketRequestModel
import kr.genti.domain.entity.response.PromptExampleModel
import kr.genti.domain.entity.response.ImageBucketModel

interface CreateRepository {
    suspend fun getSingleImageBucket(request: ImageBucketRequestModel): Result<ImageBucketModel>

    suspend fun getThreeImageBucket(request: List<ImageBucketRequestModel>): Result<List<ImageBucketModel>>

    suspend fun postToCreate(request: CreateRequestModel): Result<Boolean>

    suspend fun postToCreateOne(request: CreateRequestModel): Result<Boolean>

    suspend fun postToCreateTwo(request: CreateTwoRequestModel): Result<Boolean>

    suspend fun postToVerify(request: KeyRequestModel): Result<Boolean>

    suspend fun getPromptExample(type: String): Result<List<PromptExampleModel>>

    suspend fun postToValidatePurchase(request: PurchaseValidRequestModel): Result<Boolean>
}
