package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.ImageBucketModel

@Serializable
data class ImageBucketDto(
    @SerialName("fileName")
    val fileName: String,
    @SerialName("url")
    val presignedUrl: String,
    @SerialName("s3Key")
    val s3Key: String,
) {
    fun toModel() = ImageBucketModel(fileName, presignedUrl, s3Key)
}
