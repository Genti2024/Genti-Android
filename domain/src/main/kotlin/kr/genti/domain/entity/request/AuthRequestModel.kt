package kr.genti.domain.entity.request

data class AuthRequestModel(
    val token: String,
    val oauthPlatform: String,
)
