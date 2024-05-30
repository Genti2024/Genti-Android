package kr.genti.presentation.main.feed

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseFragment
import kr.genti.core.extension.initOnBackPressedListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentFeedBinding

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

        initOnBackPressedListener(binding.root)
        initAdapter()
        setItemList()
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

    private fun setItemList() {
        adapter.addItemList(viewModel.mockItemList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
    }
}
