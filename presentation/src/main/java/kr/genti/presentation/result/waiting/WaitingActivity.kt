package kr.genti.presentation.result.waiting

import android.app.Activity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityWaitBinding

@AndroidEntryPoint
class WaitingActivity : BaseActivity<ActivityWaitBinding>(R.layout.activity_wait) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initReturnBtnListener()
        setOnBackPressed()
        setStatusBarTransparent()
    }

    private fun initReturnBtnListener() {
        binding.btnWaitReturn.setOnSingleClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun setOnBackPressed() {
        val onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
            }
        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun setStatusBarTransparent() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            v.updatePadding(bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom)
            insets
        }
    }
}
