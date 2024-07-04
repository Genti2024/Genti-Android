package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.FeedItemModel

@Serializable
data class FeedItemDto(
    @SerialName("picture")
    val picture: ImageDto,
    @SerialName("prompt")
    val prompt: String,
) {
    fun toModel() =
        FeedItemModel(
            picture.toModel(),
            prompt,
        )
}
