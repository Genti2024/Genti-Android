package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.GenerateStatusModel
import kr.genti.domain.enums.GenerateStatus

@Serializable
data class GenerateStatusDto(
    @SerialName("pictureGenerateRequestId")
    val pictureGenerateRequestId: Long,
    @SerialName("status")
    val status: GenerateStatus,
) {
    fun toModel() =
        GenerateStatusModel(
            pictureGenerateRequestId,
            status,
        )
}
