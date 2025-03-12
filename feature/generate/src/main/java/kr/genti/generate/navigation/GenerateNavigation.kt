package kr.genti.generate.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kr.genti.generate.GenerateRoute
import kr.genti.navigation.GenerateRoute

fun NavController.navigateToGenerate(
    isParentPic: Boolean,
    navOptions: NavOptions? = null
) {
    navigate(GenerateRoute.Generate(isParentPic), navOptions)
}

fun NavGraphBuilder.generateNavGraph(
    navigateToWaiting: () -> Unit = {},
    navigateToBack: () -> Unit = {},
) {
    composable<GenerateRoute.Generate> { backStackEntry ->
        val items = backStackEntry.toRoute<GenerateRoute.Generate>()
        GenerateRoute(
            isParentPic = items.isParentPic,
            navigateToWaiting = navigateToWaiting,
            navigateToBack = navigateToBack
        )
    }
}