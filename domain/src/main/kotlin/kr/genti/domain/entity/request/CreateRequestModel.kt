package kr.genti.domain.entity.request

import kr.genti.domain.enums.CameraAngle
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.enums.ShotCoverage

data class CreateRequestModel(
    val prompt: String,
    val posePicture: KeyRequestModel?,
    val facePictureList: List<KeyRequestModel>,
    val cameraAngle: CameraAngle,
    val shotCoverage: ShotCoverage,
    val pictureRatio: PictureRatio,
)
