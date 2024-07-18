package kr.genti.presentation.result.waiting

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import androidx.activity.OnBackPressedCallback
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.colorOf
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityWaitBinding

@AndroidEntryPoint
class WaitingActivity : BaseActivity<ActivityWaitBinding>(R.layout.activity_wait) {
    private var waitingErrorDialog: WaitingErrorDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initReturnBtnListener()
        getIntentIsError()
        setOnBackPressed()
        setStatusBarTransparent()
        setEmphasizedText()
    }

    private fun initReturnBtnListener() {
        binding.btnWaitReturn.setOnSingleClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun getIntentIsError() {
        val isError = intent.getBooleanExtra(EXTRA_IS_ERROR, false)
        if (isError) {
            waitingErrorDialog = WaitingErrorDialog()
            waitingErrorDialog?.show(supportFragmentManager, DIALOG_ERROR)
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

    private fun setEmphasizedText() {
        binding.tvWaitTitle.apply {
            text =
                SpannableStringBuilder(text).apply {
                    setSpan(
                        TextAppearanceSpan(context, R.style.TextAppearance_Genti_Headline1),
                        7,
                        10,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                    setSpan(
                        ForegroundColorSpan(colorOf(R.color.genti_green)),
                        7,
                        10,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        waitingErrorDialog = null
    }

    companion object {
        private const val DIALOG_ERROR = "DIALOG_ERROR"

        private const val EXTRA_IS_ERROR = "EXTRA_IS_ERROR"

        @JvmStatic
        fun createIntent(
            context: Context,
            isError: Boolean,
        ): Intent =
            Intent(context, WaitingActivity::class.java).apply {
                putExtra(EXTRA_IS_ERROR, isError)
            }
    }
}
