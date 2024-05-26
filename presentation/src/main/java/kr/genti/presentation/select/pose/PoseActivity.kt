package kr.genti.presentation.select.pose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.setStatusBarColorFromResource
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityPoseBinding
import kr.genti.presentation.select.selfie.SelfieActivity

@AndroidEntryPoint
class PoseActivity : BaseActivity<ActivityPoseBinding>(R.layout.fragment_pose) {
    private val viewModel by viewModels<PoseViewModel>()

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
        viewModel.script = intent.getStringExtra(EXTRA_SCRIPT).orEmpty()
        viewModel.plusImage = intent.getStringExtra(EXTRA_PLUS_IMAGE).orEmpty()
    }

    private fun initBackBtnListener() {
        binding.btnBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun initExitBtnListener() {
        binding.btnExit.setOnSingleClickListener {
            finish()
        }
    }

    private fun initNextBtnListener() {
        binding.btnPoseNext.setOnSingleClickListener {
            SelfieActivity.createIntent(
                this,
                viewModel.script,
                viewModel.plusImage,
                viewModel.selectedAngle.value ?: -1,
                viewModel.selectedFrame.value ?: -1,
            ).apply { startActivity(this) }
        }
    }

    private fun setStatusBarColor() {
        setStatusBarColorFromResource(R.color.background_white)
    }

    companion object {
        private const val EXTRA_SCRIPT = "EXTRA_SCRIPT"
        private const val EXTRA_PLUS_IMAGE = "EXTRA_PLUS_IMAGE"

        @JvmStatic
        fun createIntent(
            context: Context,
            script: String,
            plusImage: String,
        ): Intent =
            Intent(context, PoseActivity::class.java).apply {
                putExtra(EXTRA_SCRIPT, script)
                putExtra(EXTRA_PLUS_IMAGE, plusImage)
            }
    }
}
