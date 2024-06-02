package kr.genti.domain.enums

enum class ShotCoverage(private val description: String) {
    FACE("얼굴만 클로즈업"),
    UPPER_BODY("허리 위로 촬영"),
    KNEE_UP("무릎 위로 촬영"),
    FULL_BODY("전신 촬영"),
    ;

    override fun toString(): String {
        return description
    }
}
