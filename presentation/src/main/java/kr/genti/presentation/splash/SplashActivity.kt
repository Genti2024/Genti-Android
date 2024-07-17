package kr.genti.presentation.splash

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.genti.core.base.BaseActivity
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivitySplashBinding

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    private val viewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeAutoLoginState()
    }

    private fun observeAutoLoginState() {
        viewModel.isAutoLogined.flowWithLifecycle(lifecycle).distinctUntilChanged()
            .onEach { isAutoLogined ->
                delay(DELAY_SPLASH)
                if (isAutoLogined) {
                }
            }.launchIn(lifecycleScope)
    }

    companion object {
        private const val DELAY_SPLASH = 3000L
    }
}
