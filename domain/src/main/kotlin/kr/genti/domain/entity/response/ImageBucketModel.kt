package kr.genti.domain.entity.response

data class ImageBucketModel(
    val fileName: String,
    val presignedUrl: String,
    val s3Key: String,
)
