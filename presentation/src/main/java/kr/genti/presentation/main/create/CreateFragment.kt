package kr.genti.presentation.main.create

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.genti.core.base.BaseFragment
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.setStatusBarColor
import kr.genti.core.state.UiState
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentCreateBinding
import kr.genti.presentation.util.AmplitudeManager
import kr.genti.presentation.util.AmplitudeManager.EVENT_CLICK_BTN
import kr.genti.presentation.util.AmplitudeManager.PROPERTY_BTN
import kr.genti.presentation.util.AmplitudeManager.PROPERTY_PAGE

@AndroidEntryPoint
class CreateFragment() : BaseFragment<FragmentCreateBinding>(R.layout.fragment_create) {
    private val viewModel by activityViewModels<CreateViewModel>()
    lateinit var navController: NavController

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initBackBtnListener()
        setCurrentFragment()
        observeProgressBar()
        observeGeneratingState()
    }

    private fun initView() {
        setStatusBarColor(R.color.background_white)
        navController = binding.fcvCreate.getFragment<NavHostFragment>().navController
    }

    private fun initBackBtnListener() {
        binding.btnBack.setOnSingleClickListener {
            when (navController.currentDestination?.id) {
                R.id.defineFragment -> return@setOnSingleClickListener

                R.id.poseFragment -> {
                    AmplitudeManager.trackEvent(
                        EVENT_CLICK_BTN,
                        mapOf(PROPERTY_PAGE to "create2"),
                        mapOf(PROPERTY_BTN to "back"),
                    )
                    navController.popBackStack()
                    viewModel.modCurrentPercent(-33)
                }

                R.id.selfieFragment -> {
                    AmplitudeManager.trackEvent(
                        EVENT_CLICK_BTN,
                        mapOf(PROPERTY_PAGE to "create3"),
                        mapOf(PROPERTY_BTN to "back"),
                    )
                    navController.popBackStack()
                    viewModel.modCurrentPercent(-34)
                }
            }
        }
    }

    private fun setCurrentFragment() {
        if (::navController.isInitialized) {
            when (viewModel.currentPercent.value) {
                66 -> navController.navigate(R.id.poseFragment)
                100 -> {
                    navController.navigate(R.id.poseFragment)
                    navController.navigate(R.id.selfieFragment)
                }

                else -> return
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

    private fun observeGeneratingState() {
        viewModel.totalGeneratingState.flowWithLifecycle(lifecycle).onEach { state ->
            if (state == UiState.Loading) {
                setStatusBarColor(R.color.background_50)
                binding.layoutLoading.isVisible = true
            } else {
                setStatusBarColor(R.color.background_white)
                binding.layoutLoading.isVisible = false
            }
        }.launchIn(lifecycleScope)
    }

    companion object {
        const val PROPERTY_PROGRESS = "progress"
    }
}
