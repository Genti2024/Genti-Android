package kr.genti.navigation

import kotlinx.serialization.Serializable

interface MainTabRoute : Route {
    @Serializable
    data object Feed : MainTabRoute

    @Serializable
    data object Generate : MainTabRoute

    @Serializable
    data object Profile : MainTabRoute
}