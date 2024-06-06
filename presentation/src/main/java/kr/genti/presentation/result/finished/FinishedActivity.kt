package kr.genti.presentation.result.finished

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.TextAppearanceSpan
import androidx.activity.viewModels
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.colorOf
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityFinishedBinding
import kr.genti.presentation.util.downloadImage

@AndroidEntryPoint
class FinishedActivity : BaseActivity<ActivityFinishedBinding>(R.layout.activity_finished) {
    private val viewModel by viewModels<FinishedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSaveBtnListener()
        initReturnBtnListener()
        initUnwantedBtnListener()
        setFinishedImage()
        setEmphasizedText()
    }

    private fun initSaveBtnListener() {
        binding.btnDownload.setOnSingleClickListener {
            downloadImage(viewModel.finishedImage.id, viewModel.finishedImage.url)
        }
    }

    private fun initReturnBtnListener() {
        binding.btnReturnMain.setOnSingleClickListener {
            finish()
        }
    }

    private fun initUnwantedBtnListener() {
        binding.btnUnwanted.setOnSingleClickListener {
            // TODO
        }
    }

    private fun setFinishedImage() {
        binding.ivFinishedImage.load(viewModel.finishedImage.url)
    }

    private fun setEmphasizedText() {
        binding.tvFinishedTitle.apply {
            text =
                SpannableStringBuilder(text).apply {
                    setSpan(
                        TextAppearanceSpan(context, R.style.TextAppearance_Genti_Headline1),
                        0,
                        11,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                    setSpan(
                        ForegroundColorSpan(colorOf(R.color.genti_green)),
                        0,
                        11,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                }
        }
    }
}
