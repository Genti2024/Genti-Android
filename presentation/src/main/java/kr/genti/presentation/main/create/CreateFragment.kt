package kr.genti.presentation.main.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
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

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initCreateBtnListener()
        initRefreshExBtnListener()
    }

    private fun initView() {
        binding.vm = viewModel
    }

    private fun initCreateBtnListener() {
        binding.btnCreateNext.setOnSingleClickListener {
            PoseActivity.createIntent(
                requireContext(),
                viewModel.script.value.orEmpty(),
            ).apply { startActivity(this) }
        }
    }

    private fun initRefreshExBtnListener() {
        binding.btnRefresh.setOnSingleClickListener {
            binding.tvCreateRandomExample.text = stringOf(R.string.create_tv_example_1)
        }
    }
}
