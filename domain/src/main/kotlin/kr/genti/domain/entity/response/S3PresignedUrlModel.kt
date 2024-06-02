package kr.genti.domain.entity.response

data class S3PresignedUrlModel(
    val fileName: String,
    val url: String,
    val s3Key: String,
)
