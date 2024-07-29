package kr.genti.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.request.ReissueRequestModel

@Serializable
data class ReissueRequestDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
) {
    companion object {
        fun ReissueRequestModel.toDto() = ReissueRequestDto(accessToken, refreshToken)
    }
}
