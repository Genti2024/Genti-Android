package kr.genti.presentation.auth.signup

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.colorOf
import kr.genti.core.extension.initOnBackPressedListener
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.presentation.R
import kr.genti.presentation.auth.onboarding.OnboardingActivity
import kr.genti.presentation.databinding.ActivitySignupBinding
import kr.genti.presentation.util.AmplitudeManager
import java.util.Calendar

@AndroidEntryPoint
class SignupActivity : BaseActivity<ActivitySignupBinding>(R.layout.activity_signup) {
    private val viewModel by viewModels<SignupViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initSubmitBtnListener()
        setYearPicker()
        setStatusBarTransparent()
        setNavigationBarGreen()
        observePostSignupResult()
    }

    private fun initView() {
        AmplitudeManager.trackEvent("view_infoget")
        binding.vm = viewModel
        initOnBackPressedListener(binding.root)
    }

    private fun initSubmitBtnListener() {
        binding.btnSubmit.setOnSingleClickListener {
            viewModel.postSignupDataToServer()
        }
    }

    private fun setYearPicker() {
        binding.npSignupBirth.apply {
            val currentYear = Calendar.getInstance()[Calendar.YEAR]
            maxValue = currentYear
            minValue = 1900
            value = currentYear
            viewModel.selectBirthYear(currentYear)
            setOnValueChangedListener { _, _, newVal ->
                viewModel.selectBirthYear(newVal)
            }
        }
    }

    private fun setStatusBarTransparent() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            v.updatePadding(bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom)
            insets
        }
    }

    private fun setNavigationBarGreen() {
        this.window.navigationBarColor = colorOf(R.color.green_5)
    }

    private fun observePostSignupResult() {
        viewModel.postSignupResult.flowWithLifecycle(lifecycle).onEach { isSuccess ->
            if (isSuccess) {
                AmplitudeManager.trackEvent("complete_infoget")
                Intent(this, OnboardingActivity::class.java).apply {
                    startActivity(this)
                }
                finish()
            } else {
                toast(stringOf(R.string.error_msg))
            }
        }.launchIn(lifecycleScope)
    }
}
