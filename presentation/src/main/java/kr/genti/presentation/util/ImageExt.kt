package kr.genti.presentation.util

/**
 * Team-Going/Going-Android
 * @author chattymin
 */

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.genti.core.extension.stringOf
import kr.genti.core.extension.toast
import kr.genti.presentation.R

fun Activity.downloadImage(
    id: Long,
    imageUrl: String,
) {
    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2 &&
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ) == PackageManager.PERMISSION_GRANTED
    ) {
        requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 200)
    } else {
        val imageFileName = "img_genti_${id}_${System.currentTimeMillis()}.jpeg"

        // 이미지 저장 폴더
        val uploadFolder =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)

        // 폴더가 없다면 생성
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs()
        }

        // Coil ImageLoader 인스턴스 생성
        val imageLoader =
            ImageLoader.Builder(this)
                .build()

        // Coil을 사용하여 이미지 다운로드 및 저장
        ProcessLifecycleOwner.get().lifecycleScope.launch(Dispatchers.IO) {
            val request =
                ImageRequest.Builder(this@downloadImage)
                    .data(imageUrl)
                    .build()

            if (imageLoader.execute(request) is SuccessResult) {
                val bitmap = imageLoader.execute(request).drawable?.toBitmap()
                val contentValues =
                    ContentValues().apply {
                        put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            put(
                                MediaStore.Images.Media.RELATIVE_PATH,
                                Environment.DIRECTORY_PICTURES,
                            )
                        }
                    }

                val contentResolver = contentResolver
                val uri =
                    contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues,
                    )

                uri?.let { imageUri ->
                    contentResolver.openOutputStream(imageUri)?.use { outputStream ->
                        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }
                }
                toast(getString(R.string.profile_image_download_success))
            } else {
                toast(stringOf(R.string.error_msg))
            }
        }
    }
}
