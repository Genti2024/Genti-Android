package kr.genti.presentation.main.create

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseFragment
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentSelfieBinding

@AndroidEntryPoint
class SelfieFragment() : BaseFragment<FragmentSelfieBinding>(R.layout.fragment_selfie) {
    private val viewModel by activityViewModels<CreateViewModel>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }
}
