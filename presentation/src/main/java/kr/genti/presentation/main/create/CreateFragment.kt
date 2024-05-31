package kr.genti.presentation.main.create

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.genti.core.base.BaseFragment
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.setStatusBarColor
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
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

        initView()
        initBackBtnListener()
        observeProgressBar()
        observeGetExamplePromptsResult()
    }

    private fun initView() {
        setStatusBarColor(R.color.background_white)
        viewModel.getExamplePromptsFromServer()
    }

    private fun initBackBtnListener() {
        binding.btnBack.setOnSingleClickListener {
            val navController = binding.fcvCreate.findNavController()
            when (navController.currentDestination?.id) {
                R.id.defineFragment -> return@setOnSingleClickListener

                R.id.poseFragment -> {
                    navController.popBackStack()
                    viewModel.modCurrentPercent(-33)
                }

                R.id.selfieFragment -> {
                    navController.popBackStack()
                    viewModel.modCurrentPercent(-34)
                }
            }
        }
    }

    private fun observeProgressBar() {
        viewModel.currentPercent.flowWithLifecycle(lifecycle).onEach { percent ->
            ObjectAnimator.ofInt(
                binding.progressbarCreate,
                PROPERTY_PROGRESS,
                binding.progressbarCreate.progress,
                percent,
            ).apply {
                duration = 300
                interpolator = LinearInterpolator()
                start()
            }
            binding.btnBack.isVisible = viewModel.currentPercent.value > 33
        }.launchIn(lifecycleScope)
    }

    private fun observeGetExamplePromptsResult() {
        viewModel.getExamplePromptResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (!result) toast(stringOf(R.string.error_msg))
        }.launchIn(lifecycleScope)
    }

    companion object {
        const val PROPERTY_PROGRESS = "progress"
    }
}
