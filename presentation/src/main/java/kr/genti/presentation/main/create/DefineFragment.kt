package kr.genti.presentation.main.create

import android.os.Bundle
import android.view.View
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
import kr.genti.core.extension.initOnBackPressedListener
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.core.state.UiState
import kr.genti.domain.entity.response.ImageFileModel
import kr.genti.domain.entity.response.emptyImageFileModel
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentDefineBinding

@AndroidEntryPoint
class DefineFragment() : BaseFragment<FragmentDefineBinding>(R.layout.fragment_define) {
    private val viewModel by activityViewModels<CreateViewModel>()
    lateinit var activityResult: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCreateBtnListener()
        initRefreshExBtnListener()
        initAddImageBtnListener()
        initDeleteBtnListener()
        setGalleryImage()
        observeGetExamplePromptsResult()
        observeGetRandomPromptState()
    }

    override fun onResume() {
        super.onResume()
        setSavedImage()
    }

    private fun initView() {
        binding.vm = viewModel
        initOnBackPressedListener(binding.root)
    }

    private fun initCreateBtnListener() {
        binding.btnCreateNext.setOnSingleClickListener {
            findNavController().navigate(R.id.action_define_to_pose)
            viewModel.modCurrentPercent(33)
        }
    }

    private fun initRefreshExBtnListener() {
        binding.btnRefresh.setOnClickListener {
            viewModel.getRandomPrompt()
        }
    }

    private fun initAddImageBtnListener() {
        with(binding) {
            btnCreatePlus.setOnSingleClickListener {
                activityResult.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
            layoutAddedImage.setOnSingleClickListener {
                activityResult.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
    }

    private fun initDeleteBtnListener() {
        binding.btnDeleteImage.setOnSingleClickListener {
            viewModel.plusImage = emptyImageFileModel()
            binding.layoutAddedImage.isVisible = false
            binding.btnDeleteImage.isVisible = false
        }
    }

    private fun setGalleryImage() {
        activityResult =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.plusImage =
                        ImageFileModel(
                            uri.hashCode().toLong(),
                            uri.getFileName(requireActivity().contentResolver).toString(),
                            uri.toString(),
                        )
                    with(binding) {
                        ivAddedImage.load(uri)
                        layoutAddedImage.isVisible = true
                        btnDeleteImage.isVisible = true
                    }
                }
            }
    }

    private fun observeGetExamplePromptsResult() {
        viewModel.getExamplePromptsResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) toast(stringOf(R.string.error_msg))
        }.launchIn(lifecycleScope)
    }

    private fun observeGetRandomPromptState() {
        viewModel.getRandomPromptState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> binding.tvCreateRandomExample.text = state.data.prompt

                else -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    private fun setSavedImage() {
        if (viewModel.plusImage.id != (-1).toLong()) {
            binding.ivAddedImage.load(viewModel.plusImage.url)
            binding.layoutAddedImage.isVisible = true
        }
    }
}
