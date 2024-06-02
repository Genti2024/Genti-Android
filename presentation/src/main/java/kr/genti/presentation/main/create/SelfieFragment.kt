package kr.genti.presentation.main.create

import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.genti.core.base.BaseFragment
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentSelfieBinding
import kotlin.math.max

@AndroidEntryPoint
class SelfieFragment() : BaseFragment<FragmentSelfieBinding>(R.layout.fragment_selfie) {
    private val viewModel by activityViewModels<CreateViewModel>()
    lateinit var activityResult: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initBackPressedListener()
        initAddImageBtnListener()
        initRequestCreateBtnListener()
        setGalleryImage()
        setBulletPointList()
        setGuideListBlur()
        observeGetS3UrlResult()
    }

    override fun onResume() {
        super.onResume()
        setSavedImages()
    }

    private fun initView() {
        binding.vm = viewModel
    }

    private fun initBackPressedListener() {
        val onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                    viewModel.modCurrentPercent(-34)
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(
            requireActivity(),
            onBackPressedCallback,
        )
    }

    private fun initAddImageBtnListener() {
        with(binding) {
            btnSelfieAdd.setOnSingleClickListener {
                activityResult.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            layoutAddedImage.setOnSingleClickListener {
                activityResult.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
    }

    private fun initRequestCreateBtnListener() {
        binding.btnSelfieNext.setOnSingleClickListener {
            viewModel.getS3PresignedUrls()
        }
    }

    private fun setGalleryImage() {
        activityResult =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) { uris ->
                if (uris.isNotEmpty()) {
                    with(viewModel) {
                        uriList = uris
                        isCompleted.value = uris.size == 3
                    }
                    val imageViews =
                        with(binding) { listOf(ivAddedImage1, ivAddedImage2, ivAddedImage3) }
                    imageViews.forEach { it.setImageDrawable(null) }
                    uris.take(3).forEachIndexed { index, uri -> imageViews[index].load(uri) }
                    binding.layoutAddedImage.isVisible = uris.isNotEmpty()
                }
            }
    }

    private fun setSavedImages() {
        if (viewModel.uriList.isNotEmpty()) {
            val imageViews =
                with(binding) { listOf(ivAddedImage1, ivAddedImage2, ivAddedImage3) }
            imageViews.forEach { it.setImageDrawable(null) }
            viewModel.uriList.take(3).forEachIndexed { index, uri -> imageViews[index].load(uri) }
            binding.layoutAddedImage.isVisible = true
        }
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

    private fun observeGetS3UrlResult() {
        viewModel.getS3UrlResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) toast(stringOf(R.string.error_msg))
        }.launchIn(lifecycleScope)
    }
}
