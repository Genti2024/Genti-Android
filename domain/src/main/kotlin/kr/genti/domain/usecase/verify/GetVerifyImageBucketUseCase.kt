package kr.genti.domain.usecase.verify

import kr.genti.domain.entity.request.ImageBucketRequestModel
import kr.genti.domain.entity.response.ImageBucketModel
import kr.genti.domain.enums.FileType
import kr.genti.domain.repository.CreateRepository
import javax.inject.Inject

class GetVerifyImageBucketUseCase @Inject constructor(
    private val createRepository: CreateRepository,
) {
    suspend operator fun invoke(
        imageName: String?,
        isDebugMode: Boolean,
    ): Result<ImageBucketModel> =
        runCatching {
            val request = ImageBucketRequestModel(
                fileType = if (isDebugMode) FileType.DEV_USER_VERIFICATION_IMAGE else FileType.USER_VERIFICATION_IMAGE,
                fileName = requireNotNull(imageName) { "imageName is null" },
            )
            val response = createRepository.getSingleImageBucket(request)
            return@runCatching response
        }
}