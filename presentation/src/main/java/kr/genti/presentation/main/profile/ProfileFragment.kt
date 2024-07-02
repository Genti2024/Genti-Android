package kr.genti.presentation.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.genti.core.base.BaseFragment
import kr.genti.core.extension.initOnBackPressedListener
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.setStatusBarColor
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.core.state.UiState
import kr.genti.domain.entity.response.ImageModel
import kr.genti.presentation.R
import kr.genti.presentation.databinding.FragmentProfileBinding
import kr.genti.presentation.setting.SettingActivity

@AndroidEntryPoint
class ProfileFragment() : BaseFragment<FragmentProfileBinding>(R.layout.fragment_profile) {
    private var _adapter: ProfileAdapter? = null
    val adapter
        get() = requireNotNull(_adapter) { getString(R.string.adapter_not_initialized_error_msg) }

    private val viewModel by activityViewModels<ProfileViewModel>()

    private var profileImageDialog: ProfileImageDialog? = null

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

    private fun initImageClickListener(item: ImageModel) {
        profileImageDialog =
            ProfileImageDialog.newInstance(item.id, item.url, item.pictureRatio?.name ?: "")
        profileImageDialog?.show(parentFragmentManager, IMAGE_VIEWER)
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
        viewModel.getGenerateStatusState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    with(binding) {
                        cvProfileNormal.isVisible = state.data != true
                        cvProfileWaiting.isVisible = state.data == true
                        if (!state.data) {
                            rvProfileNormalList.adapter = adapter
                        } else {
                            rvProfileWatingList.adapter = adapter
                        }
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
        profileImageDialog = null
    }

    companion object {
        private const val IMAGE_VIEWER = "IMAGE_VIEWER"
    }
}
