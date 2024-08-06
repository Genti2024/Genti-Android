package kr.genti.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.request.KeyRequestModel

@Serializable
data class KeyRequestDto(
    @SerialName("key")
    val key: String,
) {
    companion object {
        fun KeyRequestModel.toDto() = KeyRequestDto(key)
    }
}
