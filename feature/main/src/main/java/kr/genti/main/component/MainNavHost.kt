package kr.genti.main.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import kr.genti.feed.navigation.feedNavGraph
import kr.genti.main.navigation.MainNavigator
import kr.genti.onboarding.navigation.onboardingNavGraph
import kr.genti.profile.navigation.profileNavGraph
import kr.genti.result.navigation.resultNavGraph
import kr.genti.setting.navigation.settingNavGraph

@Composable
internal fun MainNavHost(
    paddingValues: PaddingValues,
    navigator: MainNavigator,
    modifier: Modifier = Modifier,
    startNavigateToGenerate: () -> Unit = {},
) {
    NavHost(
        modifier = modifier,
        startDestination = navigator.startDestination,
        navController = navigator.navController,
    ) {
        feedNavGraph(
            paddingValues = paddingValues
        )
        profileNavGraph(
            paddingValues = paddingValues,
            navigateToGenerate = startNavigateToGenerate,
            navigateToSetting = navigator::navigateToSetting
        )
        onboardingNavGraph(
            paddingValues = paddingValues,
            navigateToLogin = navigator::navigateToLogin,
            navigateToSignup = navigator::navigateToSignup,
            navigateToFeed = navigator::navigateToFeed,
            navigateToTutorial = navigator::navigateToTutorial,
        )
        settingNavGraph(
            paddingValues = paddingValues,
            navigateToBack = navigator::navigatePopBackStack
        )
        resultNavGraph(
            paddingValues = paddingValues,
            navigateToBack = navigator::navigateBackWithBoolean
        )
    }
}