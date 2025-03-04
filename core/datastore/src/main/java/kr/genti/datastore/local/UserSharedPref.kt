package kr.genti.datastore.local

interface UserSharedPref {
    var accessToken: String
    var refreshToken: String
    var userRole: String

    fun clearInfo()
}
