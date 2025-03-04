package kr.genti.main.navigation

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import kr.genti.feature.main.R
import kr.genti.navigation.MainTabRoute
import kr.genti.navigation.Route

enum class MainTab(
    val title: String,
    @DrawableRes val selectedIconResource: Int,
    @DrawableRes val unselectedIconResource: Int,
    val route: MainTabRoute,
) {
    FEED(
        "피드",
        R.drawable.menu_feed_selected,
        R.drawable.menu_feed_unselected,
        MainTabRoute.Feed
    ),
    GENERATE(
        "생성",
        R.drawable.menu_feed_selected,
        R.drawable.menu_feed_unselected,
        MainTabRoute.Generate
    ),
    PROFILE(
        "프로필",
        R.drawable.menu_profile_selected,
        R.drawable.menu_profile_unselected,
        MainTabRoute.Profile
    );

    companion object {
        @Composable
        fun find(predicate: @Composable (MainTabRoute) -> Boolean): MainTab? {
            return entries.find { predicate(it.route) }
        }

        @Composable
        fun contains(predicate: @Composable (Route) -> Boolean): Boolean {
            return entries.map { it.route }.any { predicate(it) }
        }
    }
}
