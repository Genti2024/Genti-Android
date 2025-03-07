package kr.genti.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kr.genti.feed.navigation.navigateToFeed
import kr.genti.navigation.OnboardingRoute
import kr.genti.navigation.Route
import kr.genti.onboarding.navigation.navigateToLogin
import kr.genti.onboarding.navigation.navigateToSplash
import kr.genti.profile.navigateToProfile

class MainNavigator(
    val navController: NavHostController,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val startDestination = OnboardingRoute.Splash

    val currentTab: MainTab?
        @Composable get() = MainTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }

    @Composable
    fun shouldShowBottomBar() = MainTab.contains {
        currentDestination?.hasRoute(it::class) == true
    }

    fun navigate(tab: MainTab) {
        val navOptions = navOptions {
            navController.currentDestination?.route?.let {
                popUpTo(startDestination) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }

        when (tab) {
            MainTab.FEED -> navController.navigateToFeed(navOptions)
            MainTab.PROFILE -> navController.navigateToProfile(navOptions)
            else -> Unit
        }
    }

    fun navigateToFeed(previous: Route) = navController.navigateToFeed(
        navOptions = navOptions { popUpTo(previous) { inclusive = true } }
    )

    fun navigateToProfile() = navController.navigateToProfile()

    // TODO
    fun navigateToGenerate() = {}

    fun navigateToSplash() = navController.navigateToSplash()

    fun navigateToLogin(previous: Route) = navController.navigateToLogin(
        navOptions = navOptions { popUpTo(previous) { inclusive = true } }
    )

    // TODO
    fun navigateToSignup(previous: Route) = {}
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
): MainNavigator = remember(navController) {
    MainNavigator(navController)
}