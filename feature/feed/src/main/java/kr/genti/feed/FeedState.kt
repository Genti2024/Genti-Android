package kr.genti.feed

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.genti.domain.entity.response.FeedItemModel

data class FeedState(
    val currentPage: Int = 0,
    val itemList: ImmutableList<FeedItemModel> = persistentListOf(),
    val isTooltipVisible: Boolean = true,
    val isLoading: Boolean = false,
)