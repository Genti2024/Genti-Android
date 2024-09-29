package kr.genti.presentation.generate.verify

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setNavigationBarColorFromResource
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.setStatusBarColorFromResource
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityVerifyBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class VerifyActivity : BaseActivity<ActivityVerifyBinding>(R.layout.activity_verify) {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarColorFromResource(R.color.verify_bg)
        setNavigationBarColorFromResource(R.color.verify_bg)
        initExitBtnListener()
        initVerifyBtnListener()
        initCameraBtnListener()
        initRetakeBtnListener()
        setRequestPermissionLauncher()
        setCameraLauncher()
    }

    private fun initExitBtnListener() {
        binding.btnExit.setOnSingleClickListener { finish() }
    }

    private fun initVerifyBtnListener() {
        // TODO
        binding.btnVerify.setOnClickListener { }
    }

    private fun initCameraBtnListener() {
        binding.btnGetCameraPhoto.setOnClickListener { checkCameraPermission() }
    }

    private fun initRetakeBtnListener() {
        binding.btnRetakePhoto.setOnClickListener { startCameraLauncher() }
    }

    private fun setRequestPermissionLauncher() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) startCameraLauncher()
            }
    }

    private fun checkCameraPermission() {
        if (isPermissionNeeded()) {
            requestCameraPermission()
        } else {
            startCameraLauncher()
        }
    }

    private fun isPermissionNeeded(): Boolean =
        ContextCompat.checkSelfPermission(
            this.applicationContext,
            Manifest.permission.CAMERA,
        ) != PackageManager.PERMISSION_GRANTED

    private fun requestCameraPermission() {
        if (isCameraPermissionAlreadyRejected()) {
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.parse("package:$packageName")
                startActivity(this)
            }
        } else {
            startPermissionLauncher()
        }
    }

    private fun isCameraPermissionAlreadyRejected(): Boolean =
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            Manifest.permission.CAMERA,
        )

    private fun setCameraLauncher() {
        cameraLauncher =
            registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
                if (isSuccess) {
                    photoUri?.let { uri ->
                        with(binding) {
                            ivPhotoTaken.setImageURI(uri)
                            layoutBeforeUpload.isVisible = false
                            layoutAfterUpload.isVisible = true
                        }
                    }
                } else {
                    photoUri?.path?.let { path ->
                        if (File(path).exists()) toast(stringOf(R.string.error_msg))
                    }
                }
            }
    }

    private fun startPermissionLauncher() {
        if (::requestPermissionLauncher.isInitialized) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            toast(stringOf(R.string.error_msg))
        }
    }

    private fun startCameraLauncher() {
        if (::cameraLauncher.isInitialized) {
            runCatching {
                createImageFile()?.let { photoFile ->
                    photoUri =
                        FileProvider.getUriForFile(
                            this,
                            "${applicationContext.packageName}.fileprovider",
                            photoFile,
                        )
                }
            }.onSuccess {
                cameraLauncher.launch(photoUri)
            }
        } else {
            toast(stringOf(R.string.error_msg))
        }
    }

    private fun createImageFile(): File? =
        File.createTempFile(
            "Genti_${getFileDateFormat()}",
            ".jpg",
            cacheDir,
        )

    private fun getFileDateFormat() = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())

    override fun onDestroy() {
        super.onDestroy()

        setStatusBarColorFromResource(R.color.white)
        setNavigationBarColorFromResource(R.color.white)
    }
}
