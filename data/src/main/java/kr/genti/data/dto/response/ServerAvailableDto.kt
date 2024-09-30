package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.ServerAvailableModel

@Serializable
data class ServerAvailableDto(
    @SerialName("status")
    val status: Boolean,
    @SerialName("message")
    val message: String?,
) {
    fun toModel() = ServerAvailableModel(status, message)
}
