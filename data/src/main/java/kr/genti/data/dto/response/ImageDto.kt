package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio.Companion.toPictureRatio

@Serializable
data class ImageDto(
    @SerialName("id")
    val id: Long,
    @SerialName("url")
    val url: String,
    @SerialName("pictureRatio")
    val pictureRatio: String,
) {
    fun toModel() =
        ImageModel(
            id,
            url,
            pictureRatio.toPictureRatio(),
        )
}
