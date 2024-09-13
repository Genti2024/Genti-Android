package kr.genti.presentation.result.openchat

import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityOpenchatBinding

@AndroidEntryPoint
class OpenchatActivity : BaseActivity<ActivityOpenchatBinding>(R.layout.activity_openchat) {
    private val viewModel by viewModels<OpenchatViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initExitBtnListener()
        setTitleTextGradation()
        setGuideTextInfo()
        observeAccessibleState()
    }

    private fun initExitBtnListener() {
        // TODO
        binding.btnExit.setOnSingleClickListener { }
    }

    private fun setTitleTextGradation() {
        // TODO
    }

    private fun setGuideTextInfo() {
        // TODO
    }

    private fun observeAccessibleState() {
        // TODO
    }
}
