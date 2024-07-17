package kr.genti.domain.repository

interface UserRepository {
    fun getAccessToken(): String

    fun getRefreshToken(): String

    fun setTokens(
        accessToken: String,
        refreshToken: String,
    )

    fun setUserId(userId: Long)

    fun clearInfo()
}
