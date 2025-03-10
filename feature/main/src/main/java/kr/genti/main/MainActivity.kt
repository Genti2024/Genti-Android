package kr.genti.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.main.navigation.rememberMainNavigator

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            val navigator = rememberMainNavigator()
            GentiTheme {
                MainRoute(navigator = navigator)
            }
        }
    }
}
