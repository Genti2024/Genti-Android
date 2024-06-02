package kr.genti.presentation.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseFragment
import kr.genti.core.extension.initOnBackPressedListener
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.setStatusBarColor
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentProfileBinding
import kr.genti.presentation.setting.SettingActivity

@AndroidEntryPoint
class ProfileFragment() : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private var _adapter: ProfileAdapter? = null
    val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private val viewModel by activityViewModels<ProfileViewModel>()
    var isWaiting = true

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initSettingBtnListener()
        setAdapterList()
        setMockImages()
        observeStatus()
    }

    private fun initView() {
        initOnBackPressedListener(binding.root)
        setStatusBarColor(R.color.green_3)
    }

    private fun setAdapterList() {
        _adapter =
            ProfileAdapter(
                imageClick = ::initImageClickListener,
            )
    }

    private fun initImageClickListener(item: Int) {
        // TODO
    }

    private fun initSettingBtnListener() {
        binding.btnSetting.setOnSingleClickListener {
            Intent(requireActivity(), SettingActivity::class.java).apply {
                startActivity(this)
            }
        }
    }

    private fun setMockImages() {
        adapter.addList(viewModel.mockItemList)
    }

    private fun observeStatus() {
        // TODO: 서버통신으로 대체
        with(binding) {
            cvProfileNormal.isVisible = isWaiting != true
            cvProfileWaiting.isVisible = isWaiting == true
            if (!isWaiting) {
                rvProfileNormalList.adapter = adapter
            } else {
                rvProfileWatingList.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
    }
}
