package kr.genti.domain.enums

enum class ImageRatio(private val description: String) {
    THREE("3:2"),
    TWO("2:3"),
    ;

    override fun toString(): String {
        return description
    }
}
