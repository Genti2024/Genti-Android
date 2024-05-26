package kr.genti.presentation.main.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseFragment
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentPoseBinding

@AndroidEntryPoint
class PoseFragment() : BaseFragment<FragmentPoseBinding>(R.layout.fragment_pose) {
    private val viewModel by activityViewModels<CreateViewModel>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initNextBtnListener()
    }

    private fun initView() {
        binding.vm = viewModel
    }

    private fun initNextBtnListener() {
        binding.btnPoseNext.setOnSingleClickListener {
            findNavController().navigate(R.id.action_pose_to_selfie)
            viewModel.modCurrentPercent(34)
        }
    }
}
