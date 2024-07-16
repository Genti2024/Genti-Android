package kr.genti.domain.entity.response

data class PicturePagedListModel(
    val totalPages: Int,
    val totalElements: Int,
    val size: Int,
    val content: List<ImageModel>,
    val number: Int,
    val sort: List<PicturePageSortModel>,
    val numberOfElements: Int,
    val pageable: PicturePageableModel,
    val first: Boolean,
    val last: Boolean,
    val empty: Boolean,
) {
    data class PicturePageSortModel(
        val direction: String,
        val property: String,
        val ignoreCase: Boolean,
        val nullHandling: String,
        val ascending: Boolean,
        val descending: Boolean,
    )

    data class PicturePageableModel(
        val offset: Int,
        val sort: List<PicturePageSortModel>,
        val pageSize: Int,
        val paged: Boolean,
        val pageNumber: Int,
        val unpaged: Boolean,
    )
}
