package kr.genti.navigation

import kotlinx.serialization.Serializable

interface GenerateRoute : Route {
    @Serializable
    data object Generate : GenerateRoute
}