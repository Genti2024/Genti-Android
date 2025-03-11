package kr.genti.generate.navigation

import androidx.compose.foundation.layout.PaddingValues
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
    paddingValues: PaddingValues,
    navigateToWaiting: () -> Unit = {},
    navigateToBack: () -> Unit = {},
) {
    composable<GenerateRoute.Generate> { backStackEntry ->
        val items = backStackEntry.toRoute<GenerateRoute.Generate>()
        GenerateRoute(
            paddingValues = paddingValues,
            isParentPic = items.isParentPic,
            navigateToWaiting = navigateToWaiting,
            navigateToBack = navigateToBack
        )
    }
}