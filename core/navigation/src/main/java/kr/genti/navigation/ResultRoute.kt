package kr.genti.navigation

import kotlinx.serialization.Serializable

interface ResultRoute {
    @Serializable
    data object Waiting : ResultRoute

    @Serializable
    data object Finished : ResultRoute
}