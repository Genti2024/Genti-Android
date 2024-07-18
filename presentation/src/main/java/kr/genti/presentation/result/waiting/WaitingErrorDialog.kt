package kr.genti.presentation.result.waiting

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kr.genti.core.base.BaseDialog
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.DialogWaitingErrorBinding

class WaitingErrorDialog :
    BaseDialog<DialogWaitingErrorBinding>(R.layout.dialog_waiting_error) {
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

        initMoveToFinishBtnListener()
    }

    private fun initMoveToFinishBtnListener() {
        binding.btnAgain.setOnSingleClickListener {
            requireActivity().finish()
            dismiss()
        }
    }
}
