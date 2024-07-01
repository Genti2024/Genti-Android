package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.FeedItemModel
import kr.genti.domain.enums.PictureRatio.Companion.toPictureRatio
import kr.genti.domain.enums.PictureType.Companion.toPictureType

@Serializable
data class FeedItemDto(
    @SerialName("picture")
    val picture: FeedPictureDto,
    @SerialName("prompt")
    val prompt: String,
) {
    @Serializable
    data class FeedPictureDto(
        @SerialName("id")
        val id: Long,
        @SerialName("url")
        val url: String,
        @SerialName("key")
        val key: String,
        @SerialName("pictureRatio")
        val pictureRatio: String,
        @SerialName("type")
        val type: String,
    )

    fun toModel() =
        FeedItemModel(
            FeedItemModel.FeedPictureModel(
                picture.id,
                picture.url,
                picture.key,
                picture.pictureRatio.toPictureRatio(),
                picture.type.toPictureType(),
            ),
            prompt,
        )
}
