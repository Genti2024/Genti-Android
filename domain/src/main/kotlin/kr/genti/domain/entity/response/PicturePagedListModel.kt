package kr.genti.domain.entity.response

data class PicturePagedListModel(
    val totalPages: Int,
    val content: List<ImageModel>,
)
