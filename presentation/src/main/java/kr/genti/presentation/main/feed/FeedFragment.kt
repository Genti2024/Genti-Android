package kr.genti.presentation.main.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
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
        setStatusBarColor(R.color.background_white)
    }

    private fun initAdapter() {
        _adapter =
            FeedAdapter(
                genBtnClick = ::initGenBtnListener,
            )
        binding.rvFeed.adapter = adapter
    }

    private fun initGenBtnListener(unit: Unit) {
        // TODO: 링크 이동
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
                    }
                },
            )
        }
    }

    private fun observeGetExampleItemsState() {
        viewModel.getExampleItemsState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> adapter.addItemList(state.data)
                is UiState.Failure -> toast(stringOf(R.string.error_msg))
                else -> return@onEach
            }
        }.launchIn(lifecycleScope)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
    }
}
