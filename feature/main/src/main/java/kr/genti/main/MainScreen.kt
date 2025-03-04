package kr.genti.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.collections.immutable.toImmutableList
import kr.genti.designsystem.theme.Black
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.main.component.MainBottomBar
import kr.genti.main.component.MainBottomBtn
import kr.genti.main.component.MainNavHost
import kr.genti.main.navigation.MainNavigator
import kr.genti.main.navigation.MainTab
import kr.genti.main.navigation.rememberMainNavigator

@Composable
internal fun MainScreen(
    navigator: MainNavigator = rememberMainNavigator(),
) {
    Box(
        Modifier.fillMaxSize()
    ) {
        Scaffold(
            bottomBar = {
                MainBottomBar(
                    visible = if (LocalInspectionMode.current) true else navigator.shouldShowBottomBar(),
                    tabs = MainTab.entries.toImmutableList(),
                    currentTab = navigator.currentTab,
                    onTabSelected = navigator::navigate
                )
            },
            content = { paddingValues ->
                if (LocalInspectionMode.current) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Black)
                    )
                } else {
                    MainNavHost(
                        paddingValues = paddingValues,
                        navigator = navigator,
                        modifier = Modifier.background(Black)
                    )
                }
            }
        )

        MainBottomBtn(
            visible = if (LocalInspectionMode.current) true else navigator.shouldShowBottomBar(),
            onButtonClick = navigator::navigateToGenerate,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview
@Composable
private fun MainScreenPreview() {
    GentiTheme {
        MainScreen()
    }
}