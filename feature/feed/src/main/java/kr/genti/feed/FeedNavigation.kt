package kr.genti.feed

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kr.genti.navigation.MainTabRoute

fun NavController.navigateToFeed(
    navOptions: NavOptions? = null
) {
    navigate(MainTabRoute.Feed, navOptions)
}

fun NavGraphBuilder.feedNavGraph(
    paddingValues: PaddingValues
) {
    composable<MainTabRoute.Feed> {
        FeedRoute(paddingValues)
    }
}
