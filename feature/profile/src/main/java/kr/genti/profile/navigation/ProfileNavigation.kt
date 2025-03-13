package kr.genti.profile.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kr.genti.navigation.MainTabRoute
import kr.genti.profile.ProfileRoute

fun NavController.navigateToProfile(
    navOptions: NavOptions? = null
) {
    navigate(MainTabRoute.Profile, navOptions)
}

fun NavGraphBuilder.profileNavGraph(
    paddingValues: PaddingValues,
    navigateToGenerate: () -> Unit = {},
    navigateToSetting: () -> Unit = {}
) {
    composable<MainTabRoute.Profile> {
        ProfileRoute(
            paddingValues = paddingValues,
            navigateToGenerate = navigateToGenerate,
            navigateToSetting = navigateToSetting
        )
    }
}
