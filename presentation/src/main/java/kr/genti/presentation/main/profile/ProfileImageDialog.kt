package kr.genti.presentation.main.profile

import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.RenderEffect
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.content.FileProvider
import androidx.core.os.bundleOf
import coil.load
import kr.genti.core.base.BaseDialog
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.presentation.R
import kr.genti.presentation.databinding.DialogProfileImageBinding
import kr.genti.presentation.util.downloadImage
import java.io.File
import java.io.FileOutputStream

class ProfileImageDialog :
    BaseDialog<DialogProfileImageBinding>(R.layout.dialog_profile_image) {
    private var imageId: Long = -1
    private var imageUrl: String = ""

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
            )
            setBackgroundDrawableResource(R.color.transparent)
        }
        setBackgroundBlur(50f)
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
    }

    private fun initDownloadBtnListener() {
        binding.btnDownload.setOnSingleClickListener {
            requireActivity().downloadImage(imageId, imageUrl)
        }
    }

    private fun initShareBtnListener() {
        binding.btnShare.setOnSingleClickListener {
            Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_STREAM, getTemporaryUri())
                type = IMAGE_TYPE
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                startActivity(Intent.createChooser(this, SHARE_IMAGE_CHOOSER))
            }
        }
    }

    private fun setBackgroundBlur(radius: Float?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requireActivity().window.decorView.rootView.let { rootView ->
                if (radius != null) {
                    val blurEffect =
                        RenderEffect.createBlurEffect(radius, radius, Shader.TileMode.REPEAT)
                    rootView.setRenderEffect(blurEffect)
                } else {
                    rootView.setRenderEffect(null)
                }
            }
        }
    }

    private fun setImage() {
        // TODO: 이미지 비율 대응
        binding.ivProfile.load(imageUrl)
    }

    private fun getTemporaryUri(): Uri {
        // Bitmap 가져온 후 임시 파일로 저장
        val tempFile = File(requireActivity().cacheDir, TEMP_FILE_NAME)
        FileOutputStream(tempFile).use { out ->
            (binding.ivProfile.drawable as BitmapDrawable).bitmap.compress(
                Bitmap.CompressFormat.PNG,
                100,
                out,
            )
        }
        // 임시 파일의 URI 반환
        return FileProvider.getUriForFile(
            requireContext(),
            FILE_PROVIDER_AUTORITY,
            tempFile,
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        setBackgroundBlur(null)
    }

    companion object {
        const val IMAGE_TYPE = "image/*"
        const val SHARE_IMAGE_CHOOSER = "SHARE_IMAGE_CHOOSER"
        const val TEMP_FILE_NAME = "shared_image.png"
        const val FILE_PROVIDER_AUTORITY = "kr.genti.android.fileprovider"

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
