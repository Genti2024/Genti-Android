package kr.genti.domain.entity.request

import kr.genti.domain.enums.CameraAngle
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.enums.ShotCoverage

data class GenerateRequestModel(
    val prompt: String,
    val postPictureKey: String,
    val facePictureKeyList: List<String>,
    val cameraAngle: CameraAngle,
    val shotCoverage: ShotCoverage,
    val pictureRatio: PictureRatio,
)
