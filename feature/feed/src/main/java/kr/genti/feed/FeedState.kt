package kr.genti.feed

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.genti.domain.entity.response.FeedItemModel

data class FeedState(
    val itemList: ImmutableList<FeedItemModel> = persistentListOf(),
    val isRefreshing: Boolean = false,
    val isTooltipVisible: Boolean = false,
    val isTooltipClosed: Boolean = false,
    val isBottomSheetVisible: Boolean = false,
    val isLoading: Boolean = false,
)