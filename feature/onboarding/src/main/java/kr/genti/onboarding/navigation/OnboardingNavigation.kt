package kr.genti.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kr.genti.navigation.OnboardingRoute
import kr.genti.onboarding.splash.SplashRoute

fun NavController.navigateToSplash(
    navOptions: NavOptions? = null
) {
    navigate(OnboardingRoute.Splash, navOptions)
}

fun NavGraphBuilder.onboardingNavGraph(
    paddingValues: PaddingValues,
    navigateToLogin: () -> Unit = {},
    navigateToFeed: () -> Unit = {},
) {
    composable<OnboardingRoute.Splash> {
        SplashRoute(
            navigateToLogin = navigateToLogin,
            navigateToFeed = navigateToFeed,
        )
    }
}
