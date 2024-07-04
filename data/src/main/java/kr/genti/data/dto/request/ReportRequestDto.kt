package kr.genti.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.request.ReportRequestModel

@Serializable
data class ReportRequestDto(
    @SerialName("pictureGenerateResponseId")
    val pictureGenerateResponseId: Long,
    @SerialName("content")
    val content: String,
) {
    companion object {
        fun ReportRequestModel.toDto() = ReportRequestDto(pictureGenerateResponseId, content)
    }
}
