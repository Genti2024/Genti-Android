package kr.genti.domain.entity.response

import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.enums.PictureType

data class ImageModel(
    val id: Long,
    val url: String,
    val key: String,
    val pictureRatio: PictureRatio?,
    val type: PictureType?,
)
