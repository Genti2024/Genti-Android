package kr.genti.domain.entity.response

import kr.genti.domain.enums.PictureRatio

data class ImageModel(
    val id: Long,
    val url: String,
    val pictureRatio: PictureRatio?,
)
