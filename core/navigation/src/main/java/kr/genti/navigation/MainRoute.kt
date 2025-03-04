package kr.genti.navigation

import kotlinx.serialization.Serializable

interface MainRoute {
    @Serializable
    data object Splash : MainRoute

    @Serializable
    data object Verify : MainRoute
}