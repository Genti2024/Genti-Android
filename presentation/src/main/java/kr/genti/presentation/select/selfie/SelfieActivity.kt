package kr.genti.presentation.select.selfie

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.setStatusBarColorFromResource
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivitySelfieBinding
import kr.genti.presentation.select.wait.WaitActivity
import kotlin.math.max

@AndroidEntryPoint
class SelfieActivity : BaseActivity<ActivitySelfieBinding>(R.layout.activity_selfie) {
    private val viewModel by viewModels<SelfieViewModel>()
    lateinit var activityResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initBackBtnListener()
        initExitBtnListener()
        initNextBtnListener()
        initAddImageBtnListener()
        setGalleryImage()
        setStatusBarColor()
        setBulletPointList()
        setGuideListBlur()
    }

    private fun initView() {
        binding.vm = viewModel
        with(viewModel) {
            script = intent.getStringExtra(EXTRA_SCRIPT).orEmpty()
            angle = intent.getIntExtra(EXTRA_ANGLE, -1)
            frame = intent.getIntExtra(EXTRA_FRAME, -1)
        }
    }

    private fun initBackBtnListener() {
        binding.btnBack.setOnSingleClickListener {
            finish()
        }
    }

    private fun initExitBtnListener() {
        binding.btnExit.setOnSingleClickListener {
            finish()
        }
    }

    private fun initNextBtnListener() {
        binding.btnSelfieNext.setOnSingleClickListener {
            Intent(this, WaitActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun initAddImageBtnListener() {
        binding.btnSelfieAdd.setOnSingleClickListener {
            val intent =
                Intent(Intent.ACTION_PICK).apply {
                    type = "image/*"
                    putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                }
            activityResult.launch(intent)
        }
    }

    private fun setGalleryImage() {
        activityResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    val clipData = result.data?.clipData
                    val count = clipData?.itemCount ?: 0
                    if (count != 3) {
                        toast(getString(R.string.selfie_toast_old_picker_limit))
                        return@registerForActivityResult
                    }
                    val uriList = (0..2).mapNotNull { index -> clipData?.getItemAt(index)?.uri }
                    // TODO: 리스트 활용
                } else {
                    toast(getString(R.string.selfie_toast_picker_error))
                }
            }
    }

    private fun setStatusBarColor() {
        setStatusBarColorFromResource(R.color.background_white)
    }

    private fun setBulletPointList() {
        val points =
            listOf(
                stringOf(R.string.selfie_tv_guide_one),
                stringOf(R.string.selfie_tv_guide_two),
                stringOf(R.string.selfie_tv_guide_three),
            )
        val spannableStringBuilder = SpannableStringBuilder()
        points.forEach { point ->
            val spannableString =
                SpannableString(point).apply {
                    setSpan(
                        BulletSpan(15),
                        0,
                        point.length,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE,
                    )
                }
            with(spannableStringBuilder) {
                append(spannableString)
                append("\n")
            }
        }
        binding.tvSelfieGuideBody.text = spannableStringBuilder
    }

    private fun setGuideListBlur() {
        with(binding) {
            svSelfieGuide.setOnScrollChangeListener { _, _, scrollY, _, _ ->
                ivSelfieBlurBottom.alpha = max(0.0, (1 - scrollY / 500f).toDouble()).toFloat()
                ivSelfieBlurTop.alpha = 1 - max(0.0, (1 - scrollY / 100f).toDouble()).toFloat()
            }
        }
    }

    companion object {
        private const val EXTRA_SCRIPT = "EXTRA_SCRIPT"
        private const val EXTRA_ANGLE = "EXTRA_POSE"
        private const val EXTRA_FRAME = "EXTRA_FRAME"

        @JvmStatic
        fun createIntent(
            context: Context,
            script: String,
            angle: Int,
            frame: Int,
        ): Intent =
            Intent(context, SelfieActivity::class.java).apply {
                putExtra(EXTRA_SCRIPT, script)
                putExtra(EXTRA_ANGLE, angle)
                putExtra(EXTRA_FRAME, frame)
            }
    }
}
