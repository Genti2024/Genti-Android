package kr.genti.domain.usecase.feed

import kr.genti.domain.entity.response.FeedItemModel
import kr.genti.domain.repository.FeedRepository
import javax.inject.Inject

class GetFeedItemListUseCase @Inject constructor(
    private val feedRepository: FeedRepository
) {
    suspend operator fun invoke(): Result<List<FeedItemModel>> =
        runCatching {
            val response = feedRepository.getExampleItems()
            return@runCatching response
        }
}