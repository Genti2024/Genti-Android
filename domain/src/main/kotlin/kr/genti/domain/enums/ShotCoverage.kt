package kr.genti.domain.enums

enum class ShotCoverage(private val description: String) {
    ANY("프레임은 자유롭게 맡길래요"),
    UPPER_BODY("바스트샷\n(상반신)"),
    KNEE_SHOT("니샷\n(무릎 위)"),
    FULL_BODY("풀샷\n(전신)"),
    ;

    override fun toString(): String {
        return description
    }
}
