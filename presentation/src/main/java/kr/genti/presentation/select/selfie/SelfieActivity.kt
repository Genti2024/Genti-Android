package kr.genti.presentation.select.selfie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.setStatusBarColorFromResource
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivitySelfieBinding

@AndroidEntryPoint
class SelfieActivity : BaseActivity<ActivitySelfieBinding>(R.layout.activity_selfie) {
    private val viewModel by viewModels<SelfieViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initBackBtnListener()
        initExitBtnListener()
        initNextBtnListener()
        setStatusBarColor()
    }

    private fun initView() {
        binding.vm = viewModel
    }

    private fun initBackBtnListener() {
        binding.btnBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun initExitBtnListener() {
        binding.btnExit.setOnSingleClickListener {
            // TODO :
            finish()
        }
    }

    private fun initNextBtnListener() {
        binding.btnPoseNext.setOnSingleClickListener {
        }
    }

    private fun setStatusBarColor() {
        setStatusBarColorFromResource(R.color.background_white)
    }

    companion object {
        private const val EXTRA_SCRIPT = "EXTRA_SCRIPT"
        private const val EXTRA_ANGLE = "EXTRA_POSE"
        private const val EXTRA_FRAME = "EXTRA_FRAME"

        @JvmStatic
        fun createIntent(
            context: Context,
            script: String,
            angle: Int,
            frame: Int,
        ): Intent =
            Intent(context, SelfieActivity::class.java).apply {
                putExtra(EXTRA_SCRIPT, script)
                putExtra(EXTRA_ANGLE, angle)
                putExtra(EXTRA_FRAME, frame)
            }
    }
}
