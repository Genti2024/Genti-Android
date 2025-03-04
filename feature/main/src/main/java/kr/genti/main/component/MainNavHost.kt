package kr.genti.main.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import kr.genti.main.navigation.MainNavigator

@Composable
internal fun MainNavHost(
    paddingValues: PaddingValues,
    navigator: MainNavigator,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        startDestination = navigator.startDestination,
        navController = navigator.navController,
    ) {

    }
}