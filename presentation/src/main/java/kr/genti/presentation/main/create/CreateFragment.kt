package kr.genti.presentation.main.create

import android.os.Bundle
import android.view.View
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseFragment
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentCreateBinding

@AndroidEntryPoint
class CreateFragment() : BaseFragment<FragmentCreateBinding>(R.layout.fragment_create) {
    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
    }
}
