package kr.genti.main

import android.content.Context
import android.content.Intent
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

        val intentData = intent.getStringExtra(EXTRA_TYPE)
        val isFromNotification = intentData != null

        setContent {
            GentiTheme {
                val navigator = rememberMainNavigator(isFromNotification = isFromNotification)
                MainRoute(navigator = navigator, intentData = intentData)
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
