package kr.genti.domain.usecase.generate

import kr.genti.domain.entity.request.PurchaseValidRequestModel
import kr.genti.domain.repository.CreateRepository
import javax.inject.Inject

class CheckPurchaseValidUseCase @Inject constructor(
    private val createRepository: CreateRepository,
) {
    suspend operator fun invoke(
        packageName: String,
        productId: String,
        purchaseToken: String,
    ): Result<Boolean> =
        runCatching {
            val request = PurchaseValidRequestModel(
                packageName = packageName,
                productId = productId,
                purchaseToken = purchaseToken
            )
            val isSuccess = createRepository.postToValidatePurchase(request)
            if (isSuccess) {
                return@runCatching true
            } else {
                throw Exception("checking purchase valid failed")
            }
        }
}