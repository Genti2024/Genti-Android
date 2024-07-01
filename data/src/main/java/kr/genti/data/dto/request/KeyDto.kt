package kr.genti.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.request.KeyModel

@Serializable
data class KeyDto(
    @SerialName("key")
    val key: String?,
)

fun KeyModel.toDto() = KeyDto(key)
