package kr.genti.profile

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kr.genti.navigation.MainTabRoute

fun NavController.navigateToProfile(
    navOptions: NavOptions? = null
) {
    navigate(MainTabRoute.Profile, navOptions)
}

fun NavGraphBuilder.profileNavGraph(
    paddingValues: PaddingValues
) {
    composable<MainTabRoute.Profile> {
        ProfileRoute(paddingValues)
    }
}
