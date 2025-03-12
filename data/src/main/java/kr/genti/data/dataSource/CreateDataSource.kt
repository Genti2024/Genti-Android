package kr.genti.data.dataSource

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.CreateRequestDto
import kr.genti.data.dto.request.CreateTwoRequestDto
import kr.genti.data.dto.request.KeyRequestDto
import kr.genti.data.dto.request.PurchaseValidRequestDto
import kr.genti.data.dto.request.ImageBucketRequestDto
import kr.genti.data.dto.response.PromptExampleDto
import kr.genti.data.dto.response.ImageBucketDto

interface CreateDataSource {
    suspend fun getSingleImageBucket(request: ImageBucketRequestDto): BaseResponse<ImageBucketDto>

    suspend fun getThreeImageBucket(request: List<ImageBucketRequestDto>): BaseResponse<List<ImageBucketDto>>

    suspend fun postToCreate(request: CreateRequestDto): BaseResponse<Boolean>

    suspend fun postToCreateOne(request: CreateRequestDto): BaseResponse<Boolean>

    suspend fun postToCreateTwo(request: CreateTwoRequestDto): BaseResponse<Boolean>

    suspend fun postToVerify(request: KeyRequestDto): BaseResponse<Boolean>

    suspend fun getPromptExample(type: String): BaseResponse<List<PromptExampleDto>>

    suspend fun postToValidatePurchase(request: PurchaseValidRequestDto): BaseResponse<Boolean>
}
