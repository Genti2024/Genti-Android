package kr.genti.domain.usecase.upload

import kr.genti.domain.repository.UploadRepository
import javax.inject.Inject

class UploadImageToBucketUseCase @Inject constructor(
    private val uploadRepository: UploadRepository,
) {
    suspend operator fun invoke(
        bucketUrl: String?,
        imageUrl: String?,
    ): Result<Unit> =
        runCatching {
            val response = uploadRepository.uploadImage(
                preSignedURL = requireNotNull(bucketUrl) { "bucketUrl is null" },
                imageUri = requireNotNull(imageUrl) { "imageUrl is null" },
            )
            return@runCatching response
        }
}