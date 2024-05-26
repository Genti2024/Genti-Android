package kr.genti.presentation.main.create

import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseFragment
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.stringOf
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
        setGalleryImage()
    }

    private fun initView() {
        binding.vm = viewModel
    }

    private fun initCreateBtnListener() {
        binding.btnCreateNext.setOnSingleClickListener {
            findNavController().navigate(R.id.action_define_to_pose)
            viewModel.modCurrentPercent(33)
        }
    }

    private fun initRefreshExBtnListener() {
        binding.btnRefresh.setOnSingleClickListener {
            binding.tvCreateRandomExample.text = stringOf(R.string.create_tv_example_1)
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

    private fun setGalleryImage() {
        activityResult =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    viewModel.plusImage = uri
                    binding.ivAddedImage.load(uri)
                    binding.layoutAddedImage.isVisible = true
                }
            }
    }
}
