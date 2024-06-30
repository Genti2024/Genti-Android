package kr.genti.domain.enums

enum class PictureType {
    PictureCompleted,
    PictureCreatedByCreator,
    PicturePose,
    PicturePost,
    PictureProfile,
    PictureUserFace,
    ResponseExample,
    ;

    companion object {
        fun String.toPictureType(): PictureType? {
            return when (this) {
                "PictureCompleted" -> PictureCompleted
                "PictureCreatedByCreator" -> PictureCreatedByCreator
                "PicturePose" -> PicturePose
                "PicturePost" -> PicturePost
                "PictureProfile" -> PictureProfile
                "PictureUserFace" -> PictureUserFace
                "ResponseExample" -> ResponseExample
                else -> null
            }
        }
    }
}
