package kr.genti.navigation

import kotlinx.serialization.Serializable

interface SettingRoute : Route {
    @Serializable
    data object Setting : SettingRoute
}