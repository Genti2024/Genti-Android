package kr.genti.domain.entity.request

import kr.genti.domain.enums.FileType

data class ImageBucketRequestModel(
    val fileType: FileType,
    val fileName: String,
)
