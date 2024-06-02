package kr.genti.data.dataSource

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.response.FeedItemDto

interface FeedDataSource {
    suspend fun getExampleItems(): BaseResponse<List<FeedItemDto>>
}
