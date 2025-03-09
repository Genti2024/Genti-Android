package kr.genti.profile

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kr.genti.domain.entity.response.ImageModel

data class ProfileState(
    val itemList: ImmutableList<ImageModel> = persistentListOf(),
    val totalPage: Int = 0,
    val currentPage: Int = -1,
    val isPagingFinish: Boolean = false,
    val detailImageUrl: String = "",
    val isDetailImageGaro: Boolean = false,
    val isDetailDialogShown: Boolean = false,
    val isGenerating: Boolean = false,
    val isLoading: Boolean = false,
)