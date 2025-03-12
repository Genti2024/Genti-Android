package kr.genti.domain.entity.request

import kr.genti.domain.enums.PictureRatio

data class CreateRequestModel(
    val prompt: String,
    val imageS3KeyList: List<KeyRequestModel>,
    val pictureRatio: PictureRatio,
)
