package kr.genti.presentation.main.profile

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import coil.load
import kr.genti.core.base.BaseDialog
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.DialogProfileImageBinding
import kr.genti.presentation.util.downloadImage

class ProfileImageDialog :
    BaseDialog<DialogProfileImageBinding>(R.layout.dialog_profile_image) {
    private var imageId: Long = -1
    private var imageUrl: String = ""

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
            )
            setBackgroundDrawableResource(R.color.transparent)
        }
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)

        getBundleArgs()
        initExitBtnListener()
        initDownloadBtnListener()
        initShareBtnListener()
        setImage()
    }

    private fun getBundleArgs() {
        arguments ?: return
        imageId = arguments?.getLong(ARGS_IMAGE_ID) ?: -1
        imageUrl = arguments?.getString(ARGS_IMAGE_URL) ?: ""
    }

    private fun initExitBtnListener() {
        binding.btnExit.setOnSingleClickListener { dismiss() }
        binding.ivProfileBackground.setOnSingleClickListener { dismiss() }
    }

    private fun initDownloadBtnListener() {
        binding.btnDownload.setOnSingleClickListener {
            requireActivity().downloadImage(imageId, imageUrl)
        }
    }

    private fun initShareBtnListener() {
        binding.btnShare.setOnSingleClickListener {
            // TODO
        }
    }

    private fun setImage() {
        // TODO: 이미지 비율 대응
        binding.ivProfile.load(imageUrl)
    }

    companion object {
        const val ARGS_IMAGE_ID = "IMAGE_ID"
        const val ARGS_IMAGE_URL = "IMAGE_URL"

        @JvmStatic
        fun newInstance(
            id: Long,
            imageUrl: String,
        ) = ProfileImageDialog().apply {
            val args =
                bundleOf(
                    ARGS_IMAGE_ID to id,
                    ARGS_IMAGE_URL to imageUrl,
                )
            arguments = args
        }
    }
}
