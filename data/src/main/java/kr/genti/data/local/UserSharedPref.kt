package kr.genti.data.local

interface UserSharedPref {
    var accessToken: String
    var refreshToken: String

    fun clearInfo()
}
