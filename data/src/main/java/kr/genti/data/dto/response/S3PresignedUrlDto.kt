package kr.genti.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.response.S3PresignedUrlModel

@Serializable
data class S3PresignedUrlDto(
    @SerialName("fileName")
    val fileName: String,
    @SerialName("url")
    val url: String,
    @SerialName("s3Key")
    val s3Key: String,
) {
    fun toModel() = S3PresignedUrlModel(fileName, url, s3Key)
}
