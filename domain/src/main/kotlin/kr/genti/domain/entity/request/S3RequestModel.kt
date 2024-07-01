package kr.genti.domain.entity.request

import kr.genti.domain.enums.FileType

data class S3RequestModel(
    val fileType: FileType,
    val fileName: String,
)
