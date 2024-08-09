package kr.genti.domain.enums

enum class PictureRatio {
    RATIO_SERO,
    RATIO_GARO,
    ;

    companion object {
        fun String.toPictureRatio(): PictureRatio {
            return if (this == "RATIO_GARO") {
                RATIO_GARO
            } else {
                RATIO_SERO
            }
        }
    }
}
