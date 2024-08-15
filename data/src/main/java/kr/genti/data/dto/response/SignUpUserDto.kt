package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.SignUpUserModel

@Serializable
data class SignUpUserDto(
    @SerialName("email")
    val email: String,
    @SerialName("lastLoginOauthPlatform")
    val lastLoginOauthPlatform: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("birthDate")
    val birthDate: String,
    @SerialName("sex")
    val sex: String,
) {
    fun toModel() = SignUpUserModel(email, lastLoginOauthPlatform, nickname, birthDate, sex)
}
