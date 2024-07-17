package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.PicturePagedListModel

@Serializable
data class PicturePagedListDto(
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("totalElements") val totalElements: Int,
    @SerialName("size") val size: Int,
    @SerialName("content") val content: List<ImageDto>,
    @SerialName("number") val number: Int,
    @SerialName("sort") val sort: List<PicturePageSortDto>,
    @SerialName("numberOfElements") val numberOfElements: Int,
    @SerialName("pageable") val pageable: PicturePageableDto,
    @SerialName("first") val first: Boolean,
    @SerialName("last") val last: Boolean,
    @SerialName("empty") val empty: Boolean,
) {
    @Serializable
    data class PicturePageSortDto(
        @SerialName("direction") val direction: String,
        @SerialName("property") val property: String,
        @SerialName("ignoreCase") val ignoreCase: Boolean,
        @SerialName("nullHandling") val nullHandling: String,
        @SerialName("ascending") val ascending: Boolean,
        @SerialName("descending") val descending: Boolean,
    ) {
        fun toModel() =
            PicturePagedListModel.PicturePageSortModel(
                direction,
                property,
                ignoreCase,
                nullHandling,
                ascending,
                descending,
            )
    }

    @Serializable
    data class PicturePageableDto(
        @SerialName("offset") val offset: Int,
        @SerialName("sort") val sort: List<PicturePageSortDto>,
        @SerialName("pageSize") val pageSize: Int,
        @SerialName("paged") val paged: Boolean,
        @SerialName("pageNumber") val pageNumber: Int,
        @SerialName("unpaged") val unpaged: Boolean,
    ) {
        fun toModel() =
            PicturePagedListModel.PicturePageableModel(
                offset,
                sort.map { it.toModel() },
                pageSize,
                paged,
                pageNumber,
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
            sort.map { it.toModel() },
            numberOfElements,
            pageable.toModel(),
            first,
            last,
            empty,
        )
}
