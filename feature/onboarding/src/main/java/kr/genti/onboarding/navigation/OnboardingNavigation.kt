package kr.genti.onboarding.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kr.genti.navigation.OnboardingRoute
import kr.genti.navigation.Route
import kr.genti.onboarding.login.LoginRoute
import kr.genti.onboarding.signup.SignupRoute
import kr.genti.onboarding.splash.SplashRoute

fun NavController.navigateToSplash(
    navOptions: NavOptions? = null
) {
    navigate(OnboardingRoute.Splash, navOptions)
}

fun NavController.navigateToLogin(
    navOptions: NavOptions? = null
) {
    navigate(OnboardingRoute.Login, navOptions)
}

fun NavController.navigateToSignup(
    navOptions: NavOptions? = null
) {
    navigate(OnboardingRoute.Signup, navOptions)
}

fun NavGraphBuilder.onboardingNavGraph(
    paddingValues: PaddingValues,
    navigateToLogin: (Route) -> Unit = {},
    navigateToSignup: (Route) -> Unit = {},
    navigateToFeed: (Route) -> Unit = {},
    navigateToTutorial: (Route) -> Unit = {},
) {
    composable<OnboardingRoute.Splash> {
        SplashRoute(
            navigateToLogin = navigateToLogin,
            navigateToFeed = navigateToFeed,
        )
    }
    composable<OnboardingRoute.Login> {
        LoginRoute(
            paddingValues = paddingValues,
            navigateToSignup = navigateToSignup,
            navigateToFeed = navigateToFeed,
        )
    }
    composable<OnboardingRoute.Signup> {
        SignupRoute(
            paddingValues = paddingValues,
            navigateToTutorial = navigateToTutorial
        )
    }
}
