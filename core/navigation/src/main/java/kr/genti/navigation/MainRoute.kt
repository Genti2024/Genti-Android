package kr.genti.navigation

import kotlinx.serialization.Serializable

interface MainRoute : Route {
    @Serializable
    data object Splash : MainRoute

    @Serializable
    data object Verify : MainRoute
}