package kr.genti.navigation

import kotlinx.serialization.Serializable

interface GenerateRoute : Route {
    @Serializable
    data object Verify : GenerateRoute

    @Serializable
    data object PromptInput : GenerateRoute

    @Serializable
    data object RatioSelect : GenerateRoute

    @Serializable
    data object ImageSelect : GenerateRoute
}