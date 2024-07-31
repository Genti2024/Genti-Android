package kr.genti.presentation.auth.onboarding

import android.os.Bundle
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityOnboardingBinding

@AndroidEntryPoint
class OnboardingActivity : BaseActivity<ActivityOnboardingBinding>(R.layout.activity_onboarding) {
    private var _onboardingAdapter: OnboardingAdapter? = null
    val onboardingAdapter
        get() = requireNotNull(_onboardingAdapter) { getString(R.string.adapter_not_initialized_error_msg) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewPager()
        initNextBtnListener()
    }

    private fun initViewPager() {
        _onboardingAdapter = OnboardingAdapter()
        binding.vpOnboarding.apply {
            adapter = onboardingAdapter
            getChildAt(0).setOnTouchListener { _, _ -> true }
        }
    }

    private fun initNextBtnListener() {
        with(binding) {
            btnNext.setOnSingleClickListener {
                vpOnboarding.currentItem = 1
                btnNext.isVisible = false
                btnFinish.isVisible = true
                tvOnboardingTitle.isVisible = false
                btnExit.isVisible = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _onboardingAdapter = null
    }
}
