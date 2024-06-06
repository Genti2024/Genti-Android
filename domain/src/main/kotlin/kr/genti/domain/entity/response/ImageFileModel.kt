package kr.genti.domain.entity.response

data class ImageFileModel(
    var id: Long,
    var name: String,
    var url: String,
)

fun emptyImageFileModel() = ImageFileModel(-1, "", "")
