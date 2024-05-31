package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.PromptModel

@Serializable
data class CreatePromptDto(
    @SerialName("id")
    val id: Long,
    @SerialName("prompt")
    val prompt: String,
) {
    fun toPromptModel() = PromptModel(id, prompt)
}
