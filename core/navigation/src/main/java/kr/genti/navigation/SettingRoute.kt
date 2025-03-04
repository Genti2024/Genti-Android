package kr.genti.navigation

import kotlinx.serialization.Serializable

interface SettingRoute {
    @Serializable
    data object Setting : SettingRoute
}