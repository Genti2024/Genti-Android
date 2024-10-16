package kr.genti.data.local

interface UserSharedPref {
    var accessToken: String
    var refreshToken: String
    var userRole: String
    var isGuideNeeded: Boolean
    var isChatAccessible: Boolean

    fun clearInfo()
}
