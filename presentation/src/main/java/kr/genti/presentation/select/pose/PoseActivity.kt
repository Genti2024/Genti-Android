package kr.genti.presentation.select.pose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setStatusBarColorFromResource
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityPoseBinding

@AndroidEntryPoint
class PoseActivity : BaseActivity<ActivityPoseBinding>(R.layout.activity_pose) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarColor()
    }

    private fun setStatusBarColor() {
        setStatusBarColorFromResource(R.color.background_white)
    }

    companion object {
        private const val EXTRA_SCRIPT = "EXTRA_SCRIPT"

        @JvmStatic
        fun createIntent(
            context: Context,
            script: String,
        ): Intent =
            Intent(context, PoseActivity::class.java).apply {
                putExtra(EXTRA_SCRIPT, script)
            }
    }
}
