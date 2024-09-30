package kr.genti.presentation.main

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.genti.core.base.BaseDialog
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.presentation.R
import kr.genti.presentation.databinding.DialogWaitingErrorBinding

class CreateErrorDialog : BaseDialog<DialogWaitingErrorBinding>(R.layout.dialog_waiting_error) {
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
            setBackgroundDrawableResource(R.color.transparent)
        }
        isCancelable = false
        dialog?.setCancelable(false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initCloseBtnListener()
        initMoveToFinishBtnListener()
        observeResetResult()
    }

    private fun initCloseBtnListener() {
        binding.btnClose.setOnSingleClickListener {
            viewModel.postResetStateToServer()
        }
    }

    private fun initMoveToFinishBtnListener() {
        binding.btnAgain.setOnSingleClickListener {
            viewModel.postResetStateToServer()
        }
    }

    private fun observeResetResult() {
        viewModel.postResetResult
            .flowWithLifecycle(lifecycle)
            .onEach { result ->
                if (!result) {
                    toast(stringOf(R.string.error_msg))
                } else {
                    dismiss()
                }
            }.launchIn(lifecycleScope)
    }
}
