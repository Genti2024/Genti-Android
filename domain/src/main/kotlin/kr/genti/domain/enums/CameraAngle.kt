package kr.genti.domain.enums

enum class CameraAngle(private val description: String) {
    ANY("앵글은 자유롭게 맡길래요"),
    HIGH("위에서 촬영"),
    MIDDLE("눈높이에서 촬영"),
    LOW("아래에서 촬영"),
    ;

    override fun toString(): String {
        return description
    }
}
