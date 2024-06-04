package kr.genti.domain.enums

enum class ShotCoverage(private val description: String) {
    NONE("프레임은 자유롭게 맡길래요"),
    UPPER_BODY("허리 위로 촬영"),
    KNEE_UP("무릎 위로 촬영"),
    FULL_BODY("전신 촬영"),
    ;

    override fun toString(): String {
        return description
    }
}
