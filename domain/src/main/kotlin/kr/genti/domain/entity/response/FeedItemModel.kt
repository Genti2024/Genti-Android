package kr.genti.domain.entity.response

data class FeedItemModel(
    val picture: FeedPictureModel,
    val prompt: String,
) {
    data class FeedPictureModel(
        val id: Long,
        val url: String,
        val key: String,
    )
}
