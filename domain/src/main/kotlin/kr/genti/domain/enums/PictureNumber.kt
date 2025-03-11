package kr.genti.domain.enums

enum class PictureNumber {
    ONE,
    TWO,
    NONE
    ;

    companion object {
        fun String.toPictureNumber(): PictureNumber =
            when (this) {
                "ONE" -> ONE
                "TWO" -> TWO
                else -> NONE
            }
    }
}