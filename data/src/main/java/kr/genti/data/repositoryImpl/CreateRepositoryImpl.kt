package kr.genti.data.repositoryImpl

import kr.genti.data.dataSource.CreateDataSource
import kr.genti.data.dto.request.CreateRequestDto.Companion.toDto
import kr.genti.data.dto.request.CreateTwoRequestDto.Companion.toDto
import kr.genti.data.dto.request.ImageBucketRequestDto.Companion.toDto
import kr.genti.data.dto.request.KeyRequestDto.Companion.toDto
import kr.genti.data.dto.request.PurchaseValidRequestDto.Companion.toDto
import kr.genti.domain.entity.request.CreateRequestModel
import kr.genti.domain.entity.request.CreateTwoRequestModel
import kr.genti.domain.entity.request.ImageBucketRequestModel
import kr.genti.domain.entity.request.KeyRequestModel
import kr.genti.domain.entity.request.PurchaseValidRequestModel
import kr.genti.domain.entity.response.ImageBucketModel
import kr.genti.domain.entity.response.PromptExampleModel
import kr.genti.domain.repository.CreateRepository
import javax.inject.Inject

class CreateRepositoryImpl
@Inject
constructor(
    private val createDataSource: CreateDataSource,
) : CreateRepository {
    override suspend fun getSingleImageBucket(request: ImageBucketRequestModel): ImageBucketModel =
        createDataSource.getSingleImageBucket(request.toDto()).response.toModel()

    override suspend fun getThreeImageBucket(request: List<ImageBucketRequestModel>): List<ImageBucketModel> =
        createDataSource.getThreeImageBucket(request.map { it.toDto() }).response.map { it.toModel() }

    override suspend fun postToCreate(request: CreateRequestModel): Boolean =
        createDataSource.postToCreate(request.toDto()).response

    override suspend fun postToCreateOne(request: CreateRequestModel): Boolean =
        createDataSource.postToCreateOne(request.toDto()).response

    override suspend fun postToCreateTwo(request: CreateTwoRequestModel): Boolean =
        createDataSource.postToCreateTwo(request.toDto()).response

    override suspend fun postToVerify(request: KeyRequestModel): Boolean =
        createDataSource.postToVerify(request.toDto()).response

    override suspend fun getPromptExample(type: String): List<PromptExampleModel> =
        createDataSource.getPromptExample(type).response.map { it.toModel() }

    override suspend fun postToValidatePurchase(request: PurchaseValidRequestModel): Boolean =
        createDataSource.postToValidatePurchase(request.toDto()).response
}
