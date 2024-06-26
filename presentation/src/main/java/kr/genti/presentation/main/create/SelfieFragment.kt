package kr.genti.presentation.main.create

import android.app.Activity
import android.content.Intent
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
import kr.genti.core.extension.getFileName
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.core.state.UiState
import kr.genti.domain.entity.response.ImageFileModel
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentSelfieBinding
import kr.genti.presentation.main.MainActivity
import kr.genti.presentation.main.feed.FeedFragment
import kr.genti.presentation.result.waiting.WaitingActivity
import kotlin.math.max

@AndroidEntryPoint
class SelfieFragment() : BaseFragment<FragmentSelfieBinding>(R.layout.fragment_selfie) {
    private val viewModel by activityViewModels<CreateViewModel>()
    private lateinit var photoPickerResult: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var waitingResult: ActivityResultLauncher<Intent>

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
        initWaitingResult()
        observeGeneratingState()
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
                photoPickerResult.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            layoutAddedImage.setOnSingleClickListener {
                photoPickerResult.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
    }

    private fun initRequestCreateBtnListener() {
        binding.btnSelfieNext.setOnSingleClickListener {
            viewModel.getS3PresignedUrls()
        }
    }

    private fun setGalleryImage() {
        photoPickerResult =
            registerForActivityResult(ActivityResultContracts.PickMultipleVisualMedia(3)) { uris ->
                if (uris.isNotEmpty()) {
                    with(viewModel) {
                        imageList =
                            uris.mapIndexed { _, uri ->
                                ImageFileModel(
                                    uri.hashCode().toLong(),
                                    uri.getFileName(requireActivity().contentResolver).toString(),
                                    uri.toString(),
                                )
                            }
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
        if (viewModel.imageList.isNotEmpty()) {
            val imageViews =
                with(binding) { listOf(ivAddedImage1, ivAddedImage2, ivAddedImage3) }
            imageViews.forEach { it.setImageDrawable(null) }
            viewModel.imageList.take(3)
                .forEachIndexed { index, imageFile -> imageViews[index].load(imageFile.url) }
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

    private fun initWaitingResult() {
        if (!::waitingResult.isInitialized) {
            waitingResult =
                registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult(),
                ) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.fcv_main, FeedFragment())
                            .commit()
                        (requireActivity() as? MainActivity)?.initBnvItemIconTintList()
                    }
                }
        }
    }

    private fun observeGeneratingState() {
        viewModel.totalGeneratingState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    waitingResult.launch(Intent(requireContext(), WaitingActivity::class.java))
                }

                is UiState.Failure -> {
                    toast(stringOf(R.string.error_msg))
                }

                else -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }
}
