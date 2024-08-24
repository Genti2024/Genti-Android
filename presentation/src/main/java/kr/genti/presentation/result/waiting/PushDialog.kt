package kr.genti.presentation.result.waiting

import android.app.Activity
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import kr.genti.core.base.BaseDialog
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.DialogPushBinding

class PushDialog :
    BaseDialog<DialogPushBinding>(R.layout.dialog_push) {
    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.WRAP_CONTENT,
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

        initCloseBtnListener()
        initGetAlarmBtnListener()
    }

    private fun initCloseBtnListener() {
        binding.btnClose.setOnSingleClickListener { dismiss() }
    }

    private fun initGetAlarmBtnListener() {
        binding.btnGetAlarm.setOnSingleClickListener {
            // 권한 설정
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        with(requireActivity()) {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
