package kr.genti.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.request.ImageBucketRequestModel
import kr.genti.domain.enums.FileType

@Serializable
data class ImageBucketRequestDto(
    @SerialName("fileType")
    val fileType: FileType,
    @SerialName("fileName")
    val fileName: String,
) {
    companion object {
        fun ImageBucketRequestModel.toDto() = ImageBucketRequestDto(fileType, fileName)
    }
}
