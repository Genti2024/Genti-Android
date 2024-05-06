package kr.genti.presentation.main.profile

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseFragment
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentProfileBinding

@AndroidEntryPoint
class ProfileFragment() : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }
}
