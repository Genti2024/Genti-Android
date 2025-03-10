package kr.genti.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.designsystem.theme.GentiTheme
import kr.genti.main.navigation.rememberMainNavigator

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            GentiTheme {
                val navigator = rememberMainNavigator()
                MainRoute(navigator = navigator)
            }
        }
    }

    companion object {
        private const val EXTRA_TYPE = "EXTRA_DEFAULT"

        @JvmStatic
        fun getIntent(
            context: Context,
            type: String? = null,
        ) = Intent(context, MainActivity::class.java).apply {
            putExtra(EXTRA_TYPE, type)
        }
    }
}
