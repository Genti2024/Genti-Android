package kr.genti.generate.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kr.genti.generate.GenerateRoute
import kr.genti.navigation.GenerateRoute

fun NavController.navigateToGenerate(
    navOptions: NavOptions? = null
) {
    navigate(GenerateRoute.Generate, navOptions)
}

fun NavGraphBuilder.generateNavGraph(
    paddingValues: PaddingValues,
    navigateToWaiting: () -> Unit = {},
    navigateToBack: () -> Unit = {},
) {
    composable<GenerateRoute.Generate> {
        GenerateRoute(
            paddingValues = paddingValues,
            navigateToWaiting = navigateToWaiting,
            navigateToBack = navigateToBack
        )
    }
}