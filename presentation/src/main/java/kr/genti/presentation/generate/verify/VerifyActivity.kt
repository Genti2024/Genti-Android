package kr.genti.presentation.generate.verify

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import kr.genti.core.base.BaseActivity
import kr.genti.core.extension.setNavigationBarColorFromResource
import kr.genti.core.extension.setOnSingleClickListener
import kr.genti.core.extension.setStatusBarColorFromResource
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.presentation.R
import kr.genti.presentation.databinding.ActivityVerifyBinding

@AndroidEntryPoint
class VerifyActivity : BaseActivity<ActivityVerifyBinding>(R.layout.activity_verify) {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStatusBarColorFromResource(R.color.verify_bg)
        setNavigationBarColorFromResource(R.color.verify_bg)
        initExitBtnListener()
        initVerifyBtnListener()
        setRequestPermissionLauncher()
        setCameraLauncher()
    }

    private fun initExitBtnListener() {
        binding.btnExit.setOnSingleClickListener { finish() }
    }

    private fun initVerifyBtnListener() {
        binding.btnGetCameraPhoto.setOnClickListener { checkCameraPermission() }
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

    private fun setRequestPermissionLauncher() {
        requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) startCameraLauncher()
            }
    }

    private fun setCameraLauncher() {
        cameraLauncher =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
            ) {
                if (it.resultCode == RESULT_OK && it.data != null) {
                    val bitmap = it.data?.extras?.get("data") as? Bitmap
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
            cameraLauncher.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
        } else {
            toast(stringOf(R.string.error_msg))
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        setStatusBarColorFromResource(R.color.white)
        setNavigationBarColorFromResource(R.color.white)
    }
}
