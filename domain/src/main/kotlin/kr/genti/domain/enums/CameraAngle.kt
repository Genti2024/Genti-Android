package kr.genti.domain.enums

enum class CameraAngle(private val description: String) {
    NONE("앵글은 자유롭게 맡길래요"),
    ABOVE("위에서 촬영"),
    EYE_LEVEL("같은 높이에서 촬영"),
    BELOW("아래에서 촬영"),
    ;

    override fun toString(): String {
        return description
    }
}
