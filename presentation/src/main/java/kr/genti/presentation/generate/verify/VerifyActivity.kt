package kr.genti.presentation.generate.verify

import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setNavigationBarColorFromResource
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.setStatusBarColorFromResource
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityVerifyBinding

@AndroidEntryPoint
class VerifyActivity : BaseActivity<ActivityVerifyBinding>(R.layout.activity_verify) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarColorFromResource(R.color.verify_bg)
        setNavigationBarColorFromResource(R.color.verify_bg)
        initExitBtnListener()
        initVerifyBtnListener()
    }

    private fun initExitBtnListener() {
        binding.btnExit.setOnSingleClickListener { finish() }
    }

    private fun initVerifyBtnListener() {
        binding.btnGetCameraPhoto.setOnClickListener { getCameraPhoto() }
    }

    private fun getCameraPhoto() {
    }

    override fun onDestroy() {
        super.onDestroy()

        setStatusBarColorFromResource(R.color.white)
        setNavigationBarColorFromResource(R.color.white)
    }
}
