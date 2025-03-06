package kr.genti.navigation

import kotlinx.serialization.Serializable

interface OnboardingRoute : Route {
    @Serializable
    data object Splash : OnboardingRoute

    @Serializable
    data object Login : OnboardingRoute

    @Serializable
    data object Signup : OnboardingRoute

    @Serializable
    data object Tutorial : OnboardingRoute
}
