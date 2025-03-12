package kr.genti.navigation

import kotlinx.serialization.Serializable

interface GenerateRoute : Route {
    @Serializable
    data class Generate(val isParentPic: Boolean) : GenerateRoute
}