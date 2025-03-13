package kr.genti.main.component

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import kr.genti.feed.navigation.feedNavGraph
import kr.genti.generate.navigation.generateNavGraph
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
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
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
            navigateToLogin = navigator::navigateToLogin,
            navigateToSignup = navigator::navigateToSignup,
            navigateToFeed = navigator::navigateToFeed,
            navigateToTutorial = navigator::navigateToTutorial,
        )
        settingNavGraph(
            navigateToBack = navigator::navigatePopBackStack
        )
        generateNavGraph(
            navigateToWaiting = navigator::navigateToWaiting,
            navigateToBack = navigator::navigatePopBackStack
        )
        resultNavGraph(
            navigateToBack = navigator::navigatePopBackStack,
            navigateToBackWithBoolean = navigator::navigateBackWithBoolean
        )
    }
}