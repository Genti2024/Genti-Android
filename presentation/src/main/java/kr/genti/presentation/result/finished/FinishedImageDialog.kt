package kr.genti.presentation.result.finished

import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import coil.load
import kr.genti.core.base.BaseDialog
import kr.genti.core.extension.setGusianBlur
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.DialogFinishedImageBinding
import kr.genti.presentation.util.downloadImage

class FinishedImageDialog :
    BaseDialog<DialogFinishedImageBinding>(R.layout.dialog_finished_image) {
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
        requireActivity().window.decorView.rootView.setGusianBlur(50f)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        initExitBtnListener()
        initDownloadBtnListener()
        setImage()
    }

    private fun initExitBtnListener() {
        binding.btnExit.setOnSingleClickListener { dismiss() }
        binding.ivProfileBackground.setOnSingleClickListener { dismiss() }
    }

    private fun initDownloadBtnListener() {
        binding.btnDownload.setOnSingleClickListener {
            requireActivity().downloadImage(viewModel.finishedImage.id, viewModel.finishedImage.url)
        }
    }

    private fun setImage() {
        // TODO: 이미지 비율 대응
        binding.ivProfile.load(viewModel.finishedImage.url)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        requireActivity().window.decorView.rootView.setGusianBlur(null)
    }
}
