package kr.genti.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.request.TokenRequestModel

@Serializable
data class TokenRequestDto(
    @SerialName("userId")
    val userId: Long,
) {
    companion object {
        fun TokenRequestModel.toDto() = TokenRequestDto(userId)
    }
}
