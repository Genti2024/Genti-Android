package kr.genti.navigation

import kotlinx.serialization.Serializable

interface ResultRoute : Route {
    @Serializable
    data object Verify : ResultRoute
    
    @Serializable
    data object Waiting : ResultRoute

    @Serializable
    data object Finished : ResultRoute
}