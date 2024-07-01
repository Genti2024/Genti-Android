package kr.genti.domain.entity.response

data class ImageFileModel(
    var id: Long,
    var name: String,
    var url: String,
) {
    companion object {
        fun emptyImageFileModel() = ImageFileModel(-1, "", "")
    }
}
