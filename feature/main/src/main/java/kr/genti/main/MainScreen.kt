package kr.genti.main

import androidx.compose.foundation.background
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.toImmutableList
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.main.component.MainBottomBar
import kr.genti.main.component.MainNavHost
import kr.genti.main.navigation.MainNavigator
import kr.genti.main.navigation.MainTab
import kr.genti.main.navigation.rememberMainNavigator

@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
) {
    Scaffold(
        bottomBar = {
            MainBottomBar(
                visible = navigator.shouldShowBottomBar(),
                tabs = MainTab.entries.toImmutableList(),
                currentTab = navigator.currentTab,
                onTabSelected = navigator::navigate
            )
        },
        content = { paddingValues ->
            MainNavHost(
                paddingValues = paddingValues,
                navigator = navigator,
                modifier = Modifier.background(Black)
            )
        }
    )
}

@Preview
@Composable
fun MainScreenPreview() {
    GentiTheme {
        MainScreen()
    }
}