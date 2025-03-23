package kr.genti.domain.repository

import kr.genti.domain.entity.request.CreateRequestModel
import kr.genti.domain.entity.request.CreateTwoRequestModel
import kr.genti.domain.entity.request.KeyRequestModel
import kr.genti.domain.entity.request.PurchaseValidRequestModel
import kr.genti.domain.entity.request.ImageBucketRequestModel
import kr.genti.domain.entity.response.PromptExampleModel
import kr.genti.domain.entity.response.ImageBucketModel

interface CreateRepository {
    suspend fun getSingleImageBucket(request: ImageBucketRequestModel): ImageBucketModel

    suspend fun getThreeImageBucket(request: List<ImageBucketRequestModel>): List<ImageBucketModel>

    suspend fun postToCreate(request: CreateRequestModel): Boolean

    suspend fun postToCreateOne(request: CreateRequestModel): Boolean

    suspend fun postToCreateTwo(request: CreateTwoRequestModel): Boolean

    suspend fun postToVerify(request: KeyRequestModel): Boolean

    suspend fun getPromptExample(type: String): List<PromptExampleModel>

    suspend fun postToValidatePurchase(request: PurchaseValidRequestModel): Boolean
}
