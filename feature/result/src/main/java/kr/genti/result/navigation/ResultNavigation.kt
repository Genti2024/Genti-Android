package kr.genti.result.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kr.genti.navigation.ResultRoute
import kr.genti.navigation.SettingRoute
import kr.genti.result.verify.VerifyRoute

fun NavController.navigateToVerify(
    navOptions: NavOptions? = null
) {
    navigate(SettingRoute.Setting, navOptions)
}

fun NavGraphBuilder.resultNavGraph(
    paddingValues: PaddingValues,
    navigateToBack: () -> Unit = {}
) {
    composable<ResultRoute.Verify> {
        VerifyRoute(
            paddingValues = paddingValues,
            navigateToBack = navigateToBack
        )
    }
}