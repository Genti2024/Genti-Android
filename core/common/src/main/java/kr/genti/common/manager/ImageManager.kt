package kr.genti.common.manager

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Environment.DIRECTORY_PICTURES
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns.DISPLAY_NAME
import android.provider.MediaStore.MediaColumns.MIME_TYPE
import android.provider.MediaStore.MediaColumns.RELATIVE_PATH
import androidx.core.content.ContextCompat
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object ImageManager {
    private lateinit var appContext: Context
    private lateinit var resolver: ContentResolver
    private lateinit var imageLoader: ImageLoader

    fun init(context: Context) {
        appContext = context
        resolver = context.contentResolver
        imageLoader = ImageLoader.Builder(context).build()
    }

    fun checkExternalStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            ContextCompat.checkSelfPermission(
                appContext, WRITE_EXTERNAL_STORAGE,
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    suspend fun saveImageToStorage(id: Long, imageUrl: String) =
        runCatching {
            withContext(Dispatchers.IO) {
                val bitmap = downloadBitmap(imageUrl) ?: throw Exception()
                val imageUri =
                    resolver.insert(setContentUri(), setMetaData(id)) ?: throw Exception()
                saveImageUri(imageUri, bitmap)
                resetPendingState(imageUri)
            }
        }

    private suspend fun downloadBitmap(imageUrl: String): Bitmap? =
        imageLoader.execute(
            ImageRequest.Builder(appContext).data(imageUrl).build()
        ).image?.toBitmap()

    private fun setContentUri() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

    private fun setMetaData(id: Long) =
        ContentValues().apply {
            put(DISPLAY_NAME, "img_genti_${id}_${System.currentTimeMillis()}.jpeg")
            put(MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(RELATIVE_PATH, DIRECTORY_PICTURES)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            } else {
                Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).mkdirs()
            }
        }

    private fun saveImageUri(imageUri: Uri, bitmap: Bitmap) {
        resolver.openOutputStream(imageUri)?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        } ?: throw Exception()
    }

    private fun resetPendingState(uri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply { put(MediaStore.Images.Media.IS_PENDING, 0) }
            resolver.update(uri, values, null, null)
        }
    }
}