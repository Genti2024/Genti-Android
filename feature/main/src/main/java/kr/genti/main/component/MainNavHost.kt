package kr.genti.main.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import kr.genti.feed.navigation.feedNavGraph
import kr.genti.main.navigation.MainNavigator
import kr.genti.onboarding.navigation.onboardingNavGraph
import kr.genti.profile.profileNavGraph

@Composable
internal fun MainNavHost(
    paddingValues: PaddingValues,
    navigator: MainNavigator,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        startDestination = navigator.startDestination,
        navController = navigator.navController,
    ) {
        feedNavGraph(paddingValues)
        profileNavGraph(paddingValues)
        onboardingNavGraph(
            paddingValues = paddingValues,
            navigateToLogin = navigator::navigateToLogin,
            navigateToSignup = navigator::navigateToSignup,
            navigateToFeed = navigator::navigateToFeed,
        )
    }
}