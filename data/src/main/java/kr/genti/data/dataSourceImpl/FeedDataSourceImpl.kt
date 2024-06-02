package kr.genti.data.dataSourceImpl

import kr.genti.data.dataSource.FeedDataSource
import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.response.FeedItemDto
import kr.genti.data.service.FeedService
import javax.inject.Inject

data class FeedDataSourceImpl
    @Inject
    constructor(
        private val feedService: FeedService,
    ) : FeedDataSource {
        override suspend fun getExampleItems(): BaseResponse<List<FeedItemDto>> = feedService.getExampleItems()
    }
