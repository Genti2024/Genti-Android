package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.PicturePagedListModel

@Serializable
data class PicturePagedListDto(
    @SerialName("totalPages") val totalPages: Int,
    @SerialName("content") val content: List<ImageDto>,
) {
    fun toModel() =
        PicturePagedListModel(
            totalPages,
            content.map { it.toModel() },
        )
}
