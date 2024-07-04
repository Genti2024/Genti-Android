package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio.Companion.toPictureRatio
import kr.genti.domain.enums.PictureType.Companion.toPictureType

@Serializable
data class ImageDto(
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
) {
    fun toModel() =
        ImageModel(
            id,
            url,
            key,
            pictureRatio.toPictureRatio(),
            type.toPictureType(),
        )
}
