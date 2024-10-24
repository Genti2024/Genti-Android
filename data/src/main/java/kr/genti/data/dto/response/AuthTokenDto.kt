package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.AuthTokenModel

@Serializable
data class AuthTokenDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("userRoleString")
    val userRoleString: String,
) {
    fun toModel() = AuthTokenModel(accessToken, refreshToken, userRoleString)
}
