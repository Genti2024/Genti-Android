package kr.genti.presentation.main.create

import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseFragment
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentPoseBinding

@AndroidEntryPoint
class PoseFragment() : BaseFragment<FragmentPoseBinding>(R.layout.fragment_pose) {
    private val viewModel by activityViewModels<CreateViewModel>()
}