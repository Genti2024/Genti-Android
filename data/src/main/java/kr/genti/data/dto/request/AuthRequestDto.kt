package kr.genti.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.request.AuthRequestModel

@Serializable
data class AuthRequestDto(
    @SerialName("token")
    val token: String,
    @SerialName("oauthPlatform")
    val oauthPlatform: String,
) {
    companion object {
        fun AuthRequestModel.toDto() = AuthRequestDto(token, oauthPlatform)
    }
}
