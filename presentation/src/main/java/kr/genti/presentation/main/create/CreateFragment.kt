package kr.genti.presentation.main.create

import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseFragment
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.stringOf
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentCreateBinding
import kr.genti.presentation.select.pose.PoseActivity

@AndroidEntryPoint
class CreateFragment() : BaseFragment<FragmentCreateBinding>(R.layout.fragment_create) {
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
            PoseActivity.createIntent(
                requireContext(),
                viewModel.script.value.orEmpty(),
                viewModel.plusImage.toString(),
            ).apply { startActivity(this) }
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
