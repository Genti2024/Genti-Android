package kr.genti.presentation.result.finished

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import kr.genti.core.base.BaseDialog
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.DialogFinishedErrorBinding

class FinishedErrorDialog :
    BaseDialog<DialogFinishedErrorBinding>(R.layout.dialog_finished_error) {
    private val viewModel by activityViewModels<FinishedViewModel>()

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

        initExitBtnListener()
        initSubmitBtnListener()
    }

    private fun initExitBtnListener() {
        with(binding) {
            btnBack.setOnSingleClickListener { dismiss() }
            btnClose.setOnSingleClickListener { dismiss() }
            btnOkay.setOnSingleClickListener { dismiss() }
        }
    }

    private fun initSubmitBtnListener() {
        binding.btnSubmit.setOnSingleClickListener {
            // TODO: 서버통신
            with(binding) {
                layoutErrorInput.isVisible = false
                layoutErrorOutput.isVisible = true
            }
        }
    }
}
