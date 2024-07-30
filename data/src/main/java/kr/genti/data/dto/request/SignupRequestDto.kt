package kr.genti.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.request.SignupRequestModel

@Serializable
data class SignupRequestDto(
    @SerialName("birthDate")
    val birthDate: String,
    @SerialName("sex")
    val sex: String,
) {
    companion object {
        fun SignupRequestModel.toDto() = SignupRequestDto(birthDate, sex)
    }
}
