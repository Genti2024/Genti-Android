package kr.genti.navigation

import kotlinx.serialization.Serializable

interface MainTabRoute {
    @Serializable
    data object Feed : MainTabRoute

    @Serializable
    data object Profile : MainTabRoute
}