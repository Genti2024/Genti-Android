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
    private var finishedImageDialog: FinishedImageDialog? = null
    private var finishedErrorDialog: FinishedErrorDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initImageBtnListener()
        initSaveBtnListener()
        initReturnBtnListener()
        initUnwantedBtnListener()
        setFinishedImage()
        setEmphasizedText()
    }

    private fun initImageBtnListener() {
        binding.ivFinishedImage.setOnSingleClickListener {
            finishedImageDialog = FinishedImageDialog()
            finishedImageDialog?.show(supportFragmentManager, DIALOG_IMAGE)
        }
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
            finishedErrorDialog = FinishedErrorDialog()
            finishedErrorDialog?.show(supportFragmentManager, DIALOG_ERROR)
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

    override fun onDestroy() {
        super.onDestroy()
        finishedImageDialog = null
        finishedErrorDialog = null
    }

    companion object {
        private const val DIALOG_IMAGE = "DIALOG_IMAGE"
        private const val DIALOG_ERROR = "DIALOG_ERROR"
    }
}
