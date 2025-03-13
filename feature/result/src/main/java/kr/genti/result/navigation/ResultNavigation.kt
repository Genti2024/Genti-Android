package kr.genti.result.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kr.genti.navigation.ResultRoute
import kr.genti.result.finished.FinishedRoute
import kr.genti.result.verify.VerifyRoute
import kr.genti.result.waiting.WaitingRoute

fun NavController.navigateToVerify(
    navOptions: NavOptions? = null
) {
    navigate(ResultRoute.Verify, navOptions)
}

fun NavController.navigateToWaiting(
    isParentPic: Boolean = false,
    navOptions: NavOptions? = null
) {
    navigate(ResultRoute.Waiting(isParentPic), navOptions)
}

fun NavController.navigateToFinished(
    responseId: Long = -1,
    imageUrl: String = "",
    isGaro: Boolean = false,
    isParentPic: Boolean = false,
    navOptions: NavOptions? = null
) {
    navigate(ResultRoute.Finished(responseId, imageUrl, isGaro, isParentPic), navOptions)
}

fun NavGraphBuilder.resultNavGraph(
    navigateToBack: () -> Unit = {},
    navigateToBackWithBoolean: (Boolean) -> Unit = {}
) {
    composable<ResultRoute.Verify> {
        VerifyRoute(
            navigateToBack = navigateToBackWithBoolean
        )
    }
    composable<ResultRoute.Waiting> { backStackEntry ->
        val items = backStackEntry.toRoute<ResultRoute.Waiting>()
        WaitingRoute(
            isParentPic = items.isParentPic,
            navigateToBack = navigateToBack
        )
    }
    composable<ResultRoute.Finished> { backStackEntry ->
        val items = backStackEntry.toRoute<ResultRoute.Finished>()
        FinishedRoute(
            responseId = items.responseId,
            imageUrl = items.imageUrl,
            isGaro = items.isGaro,
            isParentPic = items.isParentPic,
            navigateToBack = navigateToBack
        )
    }
}