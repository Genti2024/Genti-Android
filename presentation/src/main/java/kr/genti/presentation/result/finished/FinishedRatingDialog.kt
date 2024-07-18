package kr.genti.presentation.result.finished

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.genti.core.base.BaseDialog
import kr.genti.core.extension.setGusianBlur
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.presentation.R
import kr.genti.presentation.databinding.DialogFinishedRatingBinding
import kr.genti.presentation.main.MainActivity

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
        requireActivity().window.decorView.rootView.setGusianBlur(50f)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initSkipBtnListener()
        initSubmitBtnListener()
        observeResetResult()
    }

    private fun initSkipBtnListener() {
        binding.btnSkip.setOnSingleClickListener {
            viewModel.postResetStateToServer()
        }
    }

    private fun initSubmitBtnListener() {
        binding.btnSubmit.setOnSingleClickListener {
            // TODO: 서버통신
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        Intent(requireActivity(), MainActivity::class.java).apply {
            setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(this)
        }
        dismiss()
    }

    private fun observeResetResult() {
        viewModel.postResetResult.flowWithLifecycle(lifecycle).onEach { result ->
            if (result) {
                navigateToMain()
            } else {
                toast(stringOf(R.string.error_msg))
            }
        }.launchIn(lifecycleScope)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        requireActivity().window.decorView.rootView.setGusianBlur(null)
    }
}
