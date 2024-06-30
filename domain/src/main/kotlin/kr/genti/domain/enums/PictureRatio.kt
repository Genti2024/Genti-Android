package kr.genti.domain.enums

enum class PictureRatio(private val description: String) {
    RATIO_3_2("3:2 비율\n(세로로 긴 사진)"),
    RATIO_2_3("2:3 비율\n(가로로 긴 사진)"),
    ;

    override fun toString(): String {
        return description
    }

    companion object {
        fun String.toPictureRatio(): PictureRatio? {
            return when (this) {
                "RATIO_3_2" -> RATIO_3_2
                "RATIO_2_3" -> RATIO_2_3
                else -> null
            }
        }
    }
}
