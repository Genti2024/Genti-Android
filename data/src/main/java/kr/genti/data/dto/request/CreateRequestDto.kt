package kr.genti.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.request.CreateRequestModel
import kr.genti.domain.enums.CameraAngle
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.enums.ShotCoverage

@Serializable
data class CreateRequestDto(
    @SerialName("prompt")
    val prompt: String,
    @SerialName("posePicture")
    val posePicture: KeyRequestDto?,
    @SerialName("facePictureList")
    val facePictureList: List<KeyRequestDto>,
    @SerialName("cameraAngle")
    val cameraAngle: CameraAngle,
    @SerialName("shotCoverage")
    val shotCoverage: ShotCoverage,
    @SerialName("pictureRatio")
    val pictureRatio: PictureRatio,
)

fun CreateRequestModel.toDto() =
    CreateRequestDto(
        prompt,
        posePicture?.toDto(),
        facePictureList.map { it.toDto() },
        cameraAngle,
        shotCoverage,
        pictureRatio,
    )
