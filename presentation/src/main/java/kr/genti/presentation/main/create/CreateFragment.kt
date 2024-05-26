package kr.genti.presentation.main.create

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseFragment
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentCreateBinding

@AndroidEntryPoint
class CreateFragment() : BaseFragment<FragmentCreateBinding>(R.layout.fragment_create) {
    private val viewModel by activityViewModels<CreateViewModel>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initBackBtnListener()
    }

    fun initBackBtnListener() {
        binding.btnBack.setOnSingleClickListener {
            val navController = view?.findNavController()
            when (navController?.currentDestination?.id) {
                R.id.defineFragment -> return@setOnSingleClickListener

                R.id.poseFragment -> {
                    navController.popBackStack()
                    modProgressBar(-33)
                    binding.btnBack.isVisible = false
                }

                R.id.selfieFragment -> {
                    navController.popBackStack()
                    modProgressBar(-34)
                }
            }
        }
    }

    fun modProgressBar(num: Int) {
        viewModel.currentPercent += num
        val animator =
            ObjectAnimator.ofInt(
                binding.progressbarCreate,
                PROPERTY_PROGRESS,
                binding.progressbarCreate.progress,
                viewModel.currentPercent,
            )
        animator.duration = 300
        animator.interpolator = LinearInterpolator()
        animator.start()
    }

    companion object {
        const val PROPERTY_PROGRESS = "progress"
    }
}
