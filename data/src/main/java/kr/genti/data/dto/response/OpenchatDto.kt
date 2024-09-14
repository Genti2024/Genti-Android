package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.OpenchatModel

@Serializable
data class OpenchatDto(
    @SerialName("accessible")
    val accessible: Boolean,
    @SerialName("count")
    val count: Int?,
    @SerialName("url")
    val url: String?,
) {
    fun toModel() = OpenchatModel(accessible, count, url)
}
