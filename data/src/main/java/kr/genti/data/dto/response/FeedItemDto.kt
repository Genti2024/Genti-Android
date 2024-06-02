package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.FeedItemModel

@Serializable
data class FeedItemDto(
    @SerialName("id")
    val id: Long,
    @SerialName("url")
    val url: String,
    @SerialName("prompt")
    val prompt: String,
) {
    fun toModel() = FeedItemModel(id, url, prompt)
}
