package kr.genti.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kr.genti.domain.entity.request.GenerateRequestModel
import kr.genti.domain.enums.CameraAngle
import kr.genti.domain.enums.PictureRatio
import kr.genti.domain.enums.ShotCoverage

@Serializable
data class GenerateRequestDto(
    @SerialName("prompt")
    val prompt: String,
    @SerialName("postPictureKey")
    val postPictureKey: String,
    @SerialName("facePictureKeyList")
    val facePictureKeyList: List<String>,
    @SerialName("cameraAngle")
    val cameraAngle: CameraAngle,
    @SerialName("shotCoverage")
    val shotCoverage: ShotCoverage,
    @SerialName("pictureRatio")
    val pictureRatio: PictureRatio,
)

fun GenerateRequestModel.toDto() =
    GenerateRequestDto(
        prompt,
        postPictureKey,
        facePictureKeyList,
        cameraAngle,
        shotCoverage,
        pictureRatio,
    )
