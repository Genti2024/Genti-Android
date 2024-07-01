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
            return PictureType.values().find { it.toString() == this }
        }
    }
}
