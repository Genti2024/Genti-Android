package kr.genti.common.util

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

@Composable
fun DoubleBackHandler(
    navController: NavController,
    timeInterval: Long = 2_000L,
    onFirstBack: () -> Unit,
    onSecondBack: () -> Unit
) {
    var lastBackTime by remember { mutableLongStateOf(0L) }

    BackHandler {
        val now = System.currentTimeMillis()

        when {
            navController.previousBackStackEntry != null -> navController.popBackStack()

            now - lastBackTime >= timeInterval -> {
                lastBackTime = now
                onFirstBack()
            }

            else -> onSecondBack()
        }
    }
}