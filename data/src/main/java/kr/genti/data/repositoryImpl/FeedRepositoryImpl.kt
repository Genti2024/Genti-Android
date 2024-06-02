package kr.genti.data.repositoryImpl

import kr.genti.data.dataSource.FeedDataSource
import kr.genti.domain.entity.response.FeedItemModel
import kr.genti.domain.repository.FeedRepository
import javax.inject.Inject

class FeedRepositoryImpl
    @Inject
    constructor(
        private val feedDataSource: FeedDataSource,
    ) : FeedRepository {
        override suspend fun getExampleItems(): Result<List<FeedItemModel>> =
            runCatching {
                feedDataSource.getExampleItems().response.map { it.toFeedItemModel() }
            }
    }
