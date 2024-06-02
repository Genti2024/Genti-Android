package kr.genti.domain.repository

import kr.genti.domain.entity.response.FeedItemModel

interface FeedRepository {
    suspend fun getExampleItems(): Result<List<FeedItemModel>>
}
