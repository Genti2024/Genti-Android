package kr.genti.main.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kr.genti.feed.navigation.navigateToFeed
import kr.genti.generate.navigation.navigateToGenerate
import kr.genti.navigation.MainTabRoute
import kr.genti.navigation.OnboardingRoute
import kr.genti.onboarding.navigation.navigateToLogin
import kr.genti.onboarding.navigation.navigateToSignup
import kr.genti.onboarding.navigation.navigateToSplash
import kr.genti.onboarding.navigation.navigateToTutorial
import kr.genti.profile.navigation.navigateToProfile
import kr.genti.result.navigation.navigateToVerify
import kr.genti.setting.navigation.navigateToSetting

class MainNavigator(
    val navController: NavHostController,
    val isFromNotification: Boolean = false,
) {
    private val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val startDestination = if (isFromNotification) MainTabRoute.Feed else OnboardingRoute.Splash

    private val inclusiveNavOptions
        get() = navOptions {
            navController.currentDestination?.route?.let { route ->
                popUpTo(route) {
                    saveState = true
                    inclusive = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }

    val verifyResultFlow
        get() = navController.currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow<Boolean?>(IS_SUCCESS, null)
            ?: MutableStateFlow(null)

    val currentTab: MainTab?
        @Composable get() = MainTab.find { tab ->
            currentDestination?.hasRoute(tab::class) == true
        }

    @Composable
    fun shouldShowBottomBar() = MainTab.contains {
        currentDestination?.hasRoute(it::class) == true
    }

    fun navigate(tab: MainTab) {
        when (tab) {
            MainTab.FEED -> navController.navigateToFeed(inclusiveNavOptions)
            MainTab.PROFILE -> navController.navigateToProfile(inclusiveNavOptions)
            else -> Unit
        }
    }

    fun navigatePopBackStack() = navController.popBackStack()

    fun navigateBackWithBoolean(isSuccess: Boolean) {
        navController.previousBackStackEntry?.savedStateHandle?.set(IS_SUCCESS, isSuccess)
        navController.popBackStack()
    }

    fun removeBackStackEntry() =
        navController.currentBackStackEntry?.savedStateHandle?.remove<Boolean>(IS_SUCCESS)

    fun navigateToFeed() = navController.navigateToFeed(inclusiveNavOptions)

    fun navigateToProfile() = navController.navigateToProfile()

    fun navigateToSetting() = navController.navigateToSetting()

    fun navigateToGenerate() = navController.navigateToGenerate()

    fun navigateToVerify() = navController.navigateToVerify()

    fun navigateToWaiting() {

    }

    fun navigateToFinished() {

    }

    fun navigateToSplash() = navController.navigateToSplash()

    fun navigateToLogin() = navController.navigateToLogin(inclusiveNavOptions)

    fun navigateToSignup() = navController.navigateToSignup(inclusiveNavOptions)

    fun navigateToTutorial() = navController.navigateToTutorial(inclusiveNavOptions)

    companion object {
        const val IS_SUCCESS = "IS_SUCCESS"
    }
}

@Composable
internal fun rememberMainNavigator(
    navController: NavHostController = rememberNavController(),
    isFromNotification: Boolean = false,
): MainNavigator = remember(navController) {
    MainNavigator(navController, isFromNotification)
}