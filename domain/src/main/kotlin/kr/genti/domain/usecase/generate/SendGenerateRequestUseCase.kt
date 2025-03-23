package kr.genti.domain.usecase.generate

import kr.genti.domain.entity.request.CreateRequestModel
import kr.genti.domain.entity.request.CreateTwoRequestModel
import kr.genti.domain.entity.request.KeyRequestModel
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.repository.CreateRepository
import javax.inject.Inject

class SendGenerateRequestUseCase @Inject constructor(
    private val createRepository: CreateRepository,
) {
    suspend operator fun invoke(
        prompt: String,
        pictureRatio: PictureRatio,
        isParentPic: Boolean,
        imageKeyList: List<KeyRequestModel>,
    ): Result<Boolean> =
        runCatching {
            when (imageKeyList.size) {
                3 -> {
                    val request =
                        CreateRequestModel(prompt, imageKeyList.subList(0, 2), pictureRatio)
                    if (!isParentPic) {
                        createRepository.postToCreate(request)
                    } else {
                        createRepository.postToCreateOne(request)
                    }
                }

                6 -> {
                    val request = CreateTwoRequestModel(
                        prompt,
                        imageKeyList.subList(0, 2),
                        imageKeyList.subList(3, 5),
                        pictureRatio
                    )
                    createRepository.postToCreateTwo(request)
                }

                else -> throw IllegalArgumentException("Invalid number of images")
            }
        }
}