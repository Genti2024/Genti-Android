package kr.genti.data.dto.response

data class PicturePagedListDto(
    val totalPages: Int,
    val totalElements: Int,
    val size: Int,
    val content: List<ImageDto>,
    val number: Int,
    val sort: PicturePageSortModel,
    val numberOfElements: Int,
    val pageable: PicturePageableModel,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean,
) {
    data class PicturePageSortModel(
        val empty: Boolean,
        val sorted: Boolean,
        val unsorted: Boolean,
    )

    data class PicturePageableModel(
        val offset: Int,
        val sort: PicturePageSortModel,
        val paged: Boolean,
        val pageNumber: Int,
        val pageSize: Int,
        val unpaged: Boolean,
    )
}
