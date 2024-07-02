package kr.genti.presentation.setting

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kr.genti.core.base.BaseDialog
import kr.genti.core.extension.setGusianBlur
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.util.RestartUtil
import kr.genti.presentation.R
import kr.genti.presentation.databinding.DialogSettingQuitBinding

class SettingQuitDialog :
    BaseDialog<DialogSettingQuitBinding>(R.layout.dialog_setting_quit) {
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
        requireActivity().window.decorView.rootView.setGusianBlur(50f)
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
        // TODO : 토큰 설정 이후 탈퇴 설정
        binding.btnLogout.setOnSingleClickListener {
            lifecycleScope.launch {
                delay(500)
                RestartUtil.restartApp(binding.root.context, null)
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        requireActivity().window.decorView.rootView.setGusianBlur(null)
    }
}
