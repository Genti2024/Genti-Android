package kr.genti.domain.usecase.generate

import kr.genti.domain.entity.request.ImageBucketRequestModel
import kr.genti.domain.entity.response.ImageBucketModel
import kr.genti.domain.entity.response.ImageFileModel
import kr.genti.domain.enums.FileType
import kr.genti.domain.repository.CreateRepository
import javax.inject.Inject

class GetThreeImageBucketUseCase @Inject constructor(
    private val createRepository: CreateRepository,
) {
    suspend operator fun invoke(
        selectedImageList: List<ImageFileModel>
    ): Result<List<ImageBucketModel>> =
        runCatching {
            val request = selectedImageList.map { image ->
                ImageBucketRequestModel(FileType.USER_UPLOADED_IMAGE, image.name)
            }
            val response = createRepository.getThreeImageBucket(request)
            return@runCatching response
        }
}