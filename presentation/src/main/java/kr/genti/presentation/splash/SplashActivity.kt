package kr.genti.presentation.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.genti.core.base.BaseActivity
import kr.genti.presentation.R
import kr.genti.presentation.auth.LoginActivity
import kr.genti.presentation.databinding.ActivitySplashBinding
import kr.genti.presentation.main.MainActivity

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
                if (isAutoLogined) {
                    Intent(this, MainActivity::class.java).apply {
                        startActivity(this)
                    }
                } else {
                    Intent(this, LoginActivity::class.java).apply {
                        startActivity(this)
                    }
                }
                finish()
            }.launchIn(lifecycleScope)
    }
}
