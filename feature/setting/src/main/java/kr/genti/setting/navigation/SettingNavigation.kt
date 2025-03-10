package kr.genti.setting.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kr.genti.navigation.SettingRoute
import kr.genti.setting.SettingRoute

fun NavController.navigateToSetting(
    navOptions: NavOptions? = null
) {
    navigate(SettingRoute.Setting, navOptions)
}

fun NavGraphBuilder.settingNavGraph(
    paddingValues: PaddingValues,
) {
    composable<SettingRoute.Setting> {
        SettingRoute(
            paddingValues = paddingValues
        )
    }
}
