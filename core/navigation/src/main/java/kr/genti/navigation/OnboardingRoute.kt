package kr.genti.navigation

import kotlinx.serialization.Serializable

interface OnboardingRoute : Route {
    @Serializable
    data object Login : OnboardingRoute

    @Serializable
    data object SignUp : OnboardingRoute

    @Serializable
    data object Guide : OnboardingRoute
}
