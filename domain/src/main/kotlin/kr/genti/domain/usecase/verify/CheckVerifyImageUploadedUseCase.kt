package kr.genti.domain.usecase.verify

import kr.genti.domain.entity.request.KeyRequestModel
import kr.genti.domain.repository.CreateRepository
import javax.inject.Inject

class CheckVerifyImageUploadedUseCase @Inject constructor(
    private val createRepository: CreateRepository,
) {
    suspend operator fun invoke(
        bucketKey: String?,
    ): Result<Boolean> =
        runCatching {
            val request = KeyRequestModel(bucketKey)
            val isSuccess = createRepository.postToVerify(request)
            if (isSuccess) {
                return@runCatching true
            } else {
                throw Exception("checking verify image in bucket failed")
            }
        }
}