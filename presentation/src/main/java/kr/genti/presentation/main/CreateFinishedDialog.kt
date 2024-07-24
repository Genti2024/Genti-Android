package kr.genti.presentation.main

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import kr.genti.core.base.BaseDialog
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.presentation.R
import kr.genti.presentation.databinding.DialogMainFinishedBinding
import kr.genti.presentation.result.finished.FinishedActivity

class CreateFinishedDialog :
    BaseDialog<DialogMainFinishedBinding>(R.layout.dialog_main_finished) {
    private val viewModel by activityViewModels<MainViewModel>()

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

        initCloseBtnListener()
        initMoveToFinishBtnListener()
    }

    private fun initCloseBtnListener() {
        binding.btnClose.setOnSingleClickListener { dismiss() }
    }

    private fun initMoveToFinishBtnListener() {
        binding.btnMoveToFinish.setOnSingleClickListener {
            if (viewModel.checkNerPictureInitialized()) {
                FinishedActivity.createIntent(
                    requireContext(),
                    viewModel.newPicture.pictureGenerateRequestId,
                    viewModel.newPicture.pictureGenerateResponse?.pictureCompleted?.url.orEmpty(),
                    viewModel.newPicture.pictureGenerateResponse?.pictureCompleted?.pictureRatio?.name.orEmpty(),
                ).apply { startActivity(this) }
            } else {
                toast(stringOf(R.string.error_msg))
            }
            dismiss()
        }
    }
}
