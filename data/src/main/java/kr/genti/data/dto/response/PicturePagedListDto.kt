package kr.genti.data.dto.response

import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.PicturePagedListModel

@Serializable
data class PicturePagedListDto(
    val totalPages: Int,
    val totalElements: Int,
    val size: Int,
    val content: List<ImageDto>,
    val number: Int,
    val sort: PicturePageSortDto,
    val numberOfElements: Int,
    val pageable: PicturePageableDto,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean,
) {
    @Serializable
    data class PicturePageSortDto(
        val empty: Boolean,
        val sorted: Boolean,
        val unsorted: Boolean,
    ) {
        fun toModel() =
            PicturePagedListModel.PicturePageSortModel(
                empty,
                sorted,
                unsorted,
            )
    }

    @Serializable
    data class PicturePageableDto(
        val offset: Int,
        val sort: PicturePageSortDto,
        val paged: Boolean,
        val pageNumber: Int,
        val pageSize: Int,
        val unpaged: Boolean,
    ) {
        fun toModel() =
            PicturePagedListModel.PicturePageableModel(
                offset,
                sort.toModel(),
                paged,
                pageNumber,
                pageSize,
                unpaged,
            )
    }

    fun toModel() =
        PicturePagedListModel(
            totalPages,
            totalElements,
            size,
            content.map { it.toModel() },
            number,
            sort.toModel(),
            numberOfElements,
            pageable.toModel(),
            first,
            last,
            empty,
        )
}
