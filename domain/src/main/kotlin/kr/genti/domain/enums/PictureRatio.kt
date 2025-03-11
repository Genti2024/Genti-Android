package kr.genti.domain.enums

enum class PictureRatio {
    RATIO_SERO,
    RATIO_GARO,
    NONE
    ;

    companion object {
        fun String.toPictureRatio(): PictureRatio =
            when (this) {
                "RATIO_GARO" -> RATIO_GARO
                "RATIO_SERO" -> RATIO_SERO
                else -> NONE
            }
    }
}
