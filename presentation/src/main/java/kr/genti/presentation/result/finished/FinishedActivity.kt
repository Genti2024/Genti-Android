package kr.genti.presentation.result.finished

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.TypefaceSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import coil.load
import coil.transform.RoundedCornersTransformation
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.colorOf
import kr.genti.core.extension.dpToPx
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.domain.entity.response.ImageModel
import kr.genti.domain.enums.PictureRatio.Companion.toPictureRatio
import kr.genti.domain.enums.PictureType
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityFinishedBinding
import kr.genti.presentation.main.profile.ProfileImageDialog
import kr.genti.presentation.util.downloadImage
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class FinishedActivity : BaseActivity<ActivityFinishedBinding>(R.layout.activity_finished) {
    private val viewModel by viewModels<FinishedViewModel>()

    private var finishedImageDialog: FinishedImageDialog? = null
    private var finishedReportDialog: FinishedReportDialog? = null
    private var finishedRatingDialog: FinishedRatingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initImageBtnListener()
        initSaveBtnListener()
        initShareBtnListener()
        initReturnBtnListener()
        initUnwantedBtnListener()
        getIntentInfo()
        setStatusBarTransparent()
    }

    private fun initImageBtnListener() {
        binding.ivFinishedImage23.setOnSingleClickListener {
            finishedImageDialog = FinishedImageDialog()
            finishedImageDialog?.show(supportFragmentManager, DIALOG_IMAGE)
        }
        binding.ivFinishedImage32.setOnSingleClickListener {
            finishedImageDialog = FinishedImageDialog()
            finishedImageDialog?.show(supportFragmentManager, DIALOG_IMAGE)
        }
    }

    private fun initSaveBtnListener() {
        binding.btnDownload23.setOnSingleClickListener {
            downloadImage(viewModel.finishedImage.id, viewModel.finishedImage.url)
        }
        binding.btnDownload32.setOnSingleClickListener {
            downloadImage(viewModel.finishedImage.id, viewModel.finishedImage.url)
        }
    }

    private fun initShareBtnListener() {
        binding.btnShare.setOnSingleClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, getTemporaryUri())
                type = ProfileImageDialog.IMAGE_TYPE
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(this, SHARE_IMAGE_CHOOSER))
            }
        }
    }

    private fun getTemporaryUri(): Uri {
        val tempFile = File(cacheDir, ProfileImageDialog.TEMP_FILE_NAME)
        val imageView: ImageView =
            if (viewModel.isRatio23) binding.ivFinishedImage23 else binding.ivFinishedImage32
        FileOutputStream(tempFile).use { out ->
            (imageView.drawable as BitmapDrawable).bitmap.compress(
                Bitmap.CompressFormat.PNG,
                100,
                out,
            )
        }
        return FileProvider.getUriForFile(
            this,
            ProfileImageDialog.FILE_PROVIDER_AUTORITY,
            tempFile,
        )
    }

    private fun initReturnBtnListener() {
        binding.btnReturnMain.setOnSingleClickListener {
            finishedRatingDialog = FinishedRatingDialog()
            finishedRatingDialog?.show(supportFragmentManager, DIALOG_RATING)
        }
    }

    private fun initUnwantedBtnListener() {
        binding.btnUnwanted.setOnSingleClickListener {
            finishedReportDialog = FinishedReportDialog()
            finishedReportDialog?.show(supportFragmentManager, DIALOG_ERROR)
        }
    }

    private fun getIntentInfo() {
        viewModel.finishedImage =
            ImageModel(
                intent.getLongExtra(EXTRA_RESPONSE_ID, -1),
                intent.getStringExtra(EXTRA_URL) ?: "",
                "",
                intent.getStringExtra(EXTRA_RATIO)?.toPictureRatio(),
                PictureType.PictureCompleted,
            )
        viewModel.setPictureRatio()
        setUiWithRatio()
    }

    private fun setUiWithRatio() {
        with(binding) {
            layout23.isVisible = viewModel.isRatio23
            layout32.isVisible = !viewModel.isRatio23
            if (viewModel.isRatio23) {
                ivFinishedImage23.loadImageToView()
                tvFinishedTitle23.setEmphasizedText()
            } else {
                ivFinishedImage32.loadImageToView()
                tvFinishedTitle32.setEmphasizedText()
            }
        }
    }

    private fun ImageView.loadImageToView() {
        this.load(viewModel.finishedImage.url) {
            transformations(
                RoundedCornersTransformation(
                    15.dpToPx(this@FinishedActivity).toFloat(),
                ),
            )
        }
    }

    private fun TextView.setEmphasizedText() {
        this.apply {
            text =
                SpannableStringBuilder(text).apply {
                    setSpan(
                        AbsoluteSizeSpan(22, true),
                        0,
                        11,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                    setSpan(
                        ForegroundColorSpan(colorOf(R.color.green_1)),
                        0,
                        11,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                    setSpan(
                        ResourcesCompat.getFont(context, R.font.font_pretendard_bold)
                            ?.let { TypefaceSpan(it) },
                        0,
                        11,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                }
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
        finishedImageDialog = null
        finishedReportDialog = null
        finishedRatingDialog = null
    }

    companion object {
        private const val DIALOG_IMAGE = "DIALOG_IMAGE"
        private const val DIALOG_ERROR = "DIALOG_ERROR"
        private const val DIALOG_RATING = "DIALOG_RATING"
        private const val SHARE_IMAGE_CHOOSER = "SHARE_IMAGE_CHOOSER"

        private const val EXTRA_RESPONSE_ID = "EXTRA_RESPONSE_ID"
        private const val EXTRA_URL = "EXTRA_URL"
        private const val EXTRA_RATIO = "EXTRA_RATIO"

        @JvmStatic
        fun createIntent(
            context: Context,
            id: Long,
            url: String,
            ratio: String,
        ): Intent =
            Intent(context, FinishedActivity::class.java).apply {
                putExtra(EXTRA_RESPONSE_ID, id)
                putExtra(EXTRA_URL, url)
                putExtra(EXTRA_RATIO, ratio)
            }
    }
}
