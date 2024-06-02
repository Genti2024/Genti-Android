package kr.genti.presentation.result.waiting

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initReturnBtnListener()
        setStatusBarTransparent()
        setEmphasizedText()
    }

    private fun initReturnBtnListener() {
        binding.btnWaitReturn.setOnSingleClickListener {
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
}
