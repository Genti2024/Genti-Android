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
import kr.genti.onboarding.tutorial.TutorialRoute

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

fun NavController.navigateToTutorial(
    navOptions: NavOptions? = null
) {
    navigate(OnboardingRoute.Tutorial, navOptions)
}

fun NavGraphBuilder.onboardingNavGraph(
    paddingValues: PaddingValues,
    navigateToLogin: () -> Unit = {},
    navigateToSignup: () -> Unit = {},
    navigateToFeed: () -> Unit = {},
    navigateToTutorial: () -> Unit = {},
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
    composable<OnboardingRoute.Tutorial> {
        TutorialRoute(
            paddingValues = paddingValues,
            navigateToFeed = navigateToFeed,
        )
    }
}
