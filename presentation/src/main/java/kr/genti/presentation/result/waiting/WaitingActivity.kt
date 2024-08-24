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
import kr.genti.presentation.util.AmplitudeManager
import kr.genti.presentation.util.AmplitudeManager.EVENT_CLICK_BTN
import kr.genti.presentation.util.AmplitudeManager.PROPERTY_BTN
import kr.genti.presentation.util.AmplitudeManager.PROPERTY_PAGE

@AndroidEntryPoint
class WaitingActivity : BaseActivity<ActivityWaitBinding>(R.layout.activity_wait) {
    private var pushDialog: PushDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initReturnBtnListener()
        setOnBackPressed()
        setStatusBarTransparent()
    }

    private fun initReturnBtnListener() {
        binding.btnWaitReturn.setOnSingleClickListener {
            AmplitudeManager.trackEvent(
                EVENT_CLICK_BTN,
                mapOf(PROPERTY_PAGE to "picwaiting"),
                mapOf(PROPERTY_BTN to "gomain"),
            )
            startPushDialogOrFinish()
        }
    }

    private fun setOnBackPressed() {
        val onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    startPushDialogOrFinish()
                }
            }
        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    private fun startPushDialogOrFinish() {
        // 권한 체크
        if (true) {
            pushDialog = PushDialog()
            pushDialog?.show(supportFragmentManager, DIALOG_PUSH)
        } else {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun setStatusBarTransparent() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            v.updatePadding(bottom = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        pushDialog = null
    }

    companion object {
        private const val DIALOG_PUSH = "DIALOG_PUSH"
    }
}
