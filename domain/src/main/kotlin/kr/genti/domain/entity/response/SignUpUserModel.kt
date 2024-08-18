package kr.genti.domain.entity.response

data class SignUpUserModel(
    val email: String,
    val lastLoginOauthPlatform: String,
    val nickname: String,
    val birthYear: String,
    val sex: String,
)
