package kr.genti.presentation.result.finished

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import kr.genti.core.base.BaseDialog
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.DialogFinishedRatingBinding

class FinishedRatingDialog :
    BaseDialog<DialogFinishedRatingBinding>(R.layout.dialog_finished_rating) {
    private val viewModel by activityViewModels<FinishedViewModel>()

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

        initSkipBtnListener()
        initSubmitBtnListener()
    }

    private fun initSkipBtnListener() {
        binding.btnSkip.setOnSingleClickListener { dismiss() }
    }

    private fun initSubmitBtnListener() {
        binding.btnSubmit.setOnSingleClickListener {
            // TODO: 서버통신
            dismiss()
        }
    }
}
