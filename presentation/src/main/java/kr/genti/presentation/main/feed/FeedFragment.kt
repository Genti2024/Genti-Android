package kr.genti.presentation.main.feed

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.genti.core.base.BaseFragment
import kr.genti.core.extension.initOnBackPressedListener
import kr.genti.core.extension.setStatusBarColor
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.core.state.UiState
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentFeedBinding
import kr.genti.presentation.util.AmplitudeManager
import kotlin.math.max

@AndroidEntryPoint
class FeedFragment() : BaseFragment<FragmentFeedBinding>(R.layout.fragment_feed) {
    private var _adapter: FeedAdapter? = null
    val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private val viewModel by activityViewModels<FeedViewModel>()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initAdapter()
        setLightningVisibility()
        observeGetExampleItemsState()
    }

    private fun initView() {
        initOnBackPressedListener(binding.root)
        setStatusBarColor(R.color.background_50)
        viewModel.getExamplePromptsFromServer()
    }

    private fun initAdapter() {
        _adapter =
            FeedAdapter(
                genBtnClick = ::initGenBtnListener,
            )
        binding.rvFeed.adapter = adapter
    }

    private fun initGenBtnListener(x: Boolean) {
        Intent(Intent.ACTION_VIEW, Uri.parse(WEB_GENFLUENCER)).apply {
            startActivity(this)
        }
    }

    private fun setLightningVisibility() {
        with(binding) {
            rvFeed.addOnScrollListener(
                object : RecyclerView.OnScrollListener() {
                    var accumScrollY = 0

                    override fun onScrolled(
                        recyclerView: RecyclerView,
                        dx: Int,
                        dy: Int,
                    ) {
                        super.onScrolled(recyclerView, dx, dy)
                        accumScrollY += dy
                        ivFeedLightning.alpha =
                            max(0.0, (1 - accumScrollY / 130f).toDouble()).toFloat()
                        if (accumScrollY > 4500 && !viewModel.isAmplitudeScrollTracked) {
                            AmplitudeManager.apply {
                                trackEvent("scroll_main_3pic")
                                plusIntProperties("user_main_scroll")
                            }
                            viewModel.isAmplitudeScrollTracked = true
                        }
                    }
                },
            )
        }
    }

    private fun observeGetExampleItemsState() {
        viewModel.getExampleItemsState.flowWithLifecycle(lifecycle).distinctUntilChanged()
            .onEach { state ->
                when (state) {
                    is UiState.Success -> {
                        adapter.setItemList(state.data)
                        delay(500)
                        setStatusBarColor(R.color.background_white)
                        with(binding) {
                            layoutLoading.isVisible = false
                            rvFeed.isVisible = true
                            ivFeedLightning.isVisible = true
                        }
                    }

                    is UiState.Failure -> toast(stringOf(R.string.error_msg))
                    else -> return@onEach
                }
            }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
    }

    companion object {
        private const val WEB_GENFLUENCER =
            "https://stealth-goose-156.notion.site/57a00e1d610b4c1786c6ab1fdb4c4659?pvs=4"
    }
}
