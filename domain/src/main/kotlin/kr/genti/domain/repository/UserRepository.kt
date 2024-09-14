package kr.genti.domain.repository

interface UserRepository {
    fun getAccessToken(): String

    fun getRefreshToken(): String

    fun getUserRole(): String

    fun getIsGuideNeeded(): Boolean

    fun getIsChatAccessible(): Boolean

    fun setTokens(
        accessToken: String,
        refreshToken: String,
    )

    fun setUserRole(userRole: String)

    fun setIsGuideNeeded(isGuideNeeded: Boolean)

    fun setIsChatAccessible(isChatAccessible: Boolean)

    fun clearInfo()
}
