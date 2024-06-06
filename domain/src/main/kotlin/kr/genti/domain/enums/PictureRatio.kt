package kr.genti.domain.enums

enum class PictureRatio(private val description: String) {
    THREE_TWO("3:2"),
    TWO_THREE("2:3"),
    ;

    override fun toString(): String {
        return description
    }
}
