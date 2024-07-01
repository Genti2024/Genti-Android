package kr.genti.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.request.S3RequestModel
import kr.genti.domain.enums.FileType

@Serializable
data class S3RequestDto(
    @SerialName("fileType")
    val fileType: FileType,
    @SerialName("fileName")
    val fileName: String,
)

fun S3RequestModel.toDto() = S3RequestDto(fileType, fileName)
