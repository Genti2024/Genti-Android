package kr.genti.presentation.setting

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.genti.core.base.BaseDialog
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.util.RestartUtil.restartApp
import kr.genti.presentation.R
import kr.genti.presentation.databinding.DialogSettingLogoutBinding

class SettingLogoutDialog :
    BaseDialog<DialogSettingLogoutBinding>(R.layout.dialog_setting_logout) {
    private val viewModel by activityViewModels<SettingViewModel>()

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
            setBackgroundDrawableResource(R.color.transparent)
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initReturnBtnListener()
        initLogoutBtnListener()
    }

    private fun initReturnBtnListener() {
        binding.btnReturn.setOnSingleClickListener { dismiss() }
    }

    private fun initLogoutBtnListener() {
        // TODO : 토큰 설정 이후 로그아웃 설정
        binding.btnLogout.setOnSingleClickListener {
            lifecycleScope.launch {
                delay(500)
                restartApp(binding.root.context, null)
            }
        }
    }
}
