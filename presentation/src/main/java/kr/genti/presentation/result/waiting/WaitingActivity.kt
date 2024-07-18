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
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityWaitBinding

@AndroidEntryPoint
class WaitingActivity : BaseActivity<ActivityWaitBinding>(R.layout.activity_wait) {
    private val viewModel by viewModels<WaitingViewModel>()

    private var waitingErrorDialog: WaitingErrorDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initReturnBtnListener()
        getIntentIsError()
        setOnBackPressed()
        setStatusBarTransparent()
        setEmphasizedText()
        observeResetResult()
    }

    private fun initReturnBtnListener() {
        binding.btnWaitReturn.setOnSingleClickListener {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun getIntentIsError() {
        if (intent.getBooleanExtra(EXTRA_IS_ERROR, false)) {
            waitingErrorDialog = WaitingErrorDialog()
            waitingErrorDialog?.show(supportFragmentManager, DIALOG_ERROR)
            viewModel.postResetStateToServer(intent.getIntExtra(EXTRA_RESPONSE_ID, -1))
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

    private fun observeResetResult() {
        viewModel.postResetResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) toast(stringOf(R.string.error_msg))
        }.launchIn(lifecycleScope)
    }

    override fun onDestroy() {
        super.onDestroy()
        waitingErrorDialog = null
    }

    companion object {
        private const val DIALOG_ERROR = "DIALOG_ERROR"

        private const val EXTRA_IS_ERROR = "EXTRA_IS_ERROR"
        private const val EXTRA_RESPONSE_ID = "EXTRA_RESPONSE_ID"

        @JvmStatic
        fun createIntent(
            context: Context,
            isError: Boolean,
            generateResponseId: Int,
        ): Intent =
            Intent(context, WaitingActivity::class.java).apply {
                putExtra(EXTRA_IS_ERROR, isError)
                putExtra(EXTRA_RESPONSE_ID, generateResponseId)
            }
    }
}
