package kr.genti.navigation

import kotlinx.serialization.Serializable

interface ResultRoute : Route {
    @Serializable
    data object Verify : ResultRoute

    @Serializable
    data class Waiting(val isParentPic: Boolean) : ResultRoute

    @Serializable
    data object Finished : ResultRoute
}