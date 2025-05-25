package kr.genti.common.manager

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.Intent.createChooser
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
import androidx.core.content.FileProvider
import coil3.ImageLoader
import coil3.request.ImageRequest
import coil3.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kr.genti.common.extension.getFileName
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object ImageManager {
    private lateinit var appContext: Context
    private lateinit var resolver: ContentResolver
    private lateinit var coilImageLoader: ImageLoader

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")

    fun init(context: Context) {
        appContext = context
        resolver = context.contentResolver
        coilImageLoader = ImageLoader.Builder(context).build()
    }

    /**
     * 외부 저장소 쓰기 권한을 확인하는 함수
     *
     * Android S_V2 이하 버전에서는 WRITE_EXTERNAL_STORAGE 권한을 직접 확인하며, 이후 버전에서는 Scoped Storage 정책으로 인해 별도의 권한 확인 없이 true를 반환
     */
    fun checkExternalStoragePermission(): Boolean {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            ContextCompat.checkSelfPermission(
                appContext, WRITE_EXTERNAL_STORAGE,
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    /**
     * 주어진 URL에서 이미지를 다운로드 받아 MediaStore를 사용하여 외부 저장소에 저장하는 함수
     *
     * 이미지 다운로드, MediaStore에 이미지 삽입, 출력 스트림을 통해 이미지 저장, pending 상태 리셋 작업을 순차적으로 진행
     */
    suspend fun saveImageToStorage(id: Long, imageUrl: String) =
        runCatching {
            withContext(Dispatchers.IO) {
                val bitmap = downloadBitmapFromUrl(imageUrl) ?: throw Exception()
                val imageUri =
                    resolver.insert(setContentUri(), setMetaData(id)) ?: throw Exception()
                resolver.openOutputStream(imageUri).saveBitmapToFile(bitmap)
                resetPendingState(imageUri)
            }
        }

    private fun setContentUri() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

    private fun setMetaData(id: Long) =
        ContentValues().apply {
            put(DISPLAY_NAME, createGentiFileName(id))
            put(MIME_TYPE, "image/jpeg")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(RELATIVE_PATH, DIRECTORY_PICTURES)
                put(MediaStore.Images.Media.IS_PENDING, 1)
            } else {
                Environment.getExternalStoragePublicDirectory(DIRECTORY_PICTURES).mkdirs()
            }
        }

    private fun resetPendingState(uri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply { put(MediaStore.Images.Media.IS_PENDING, 0) }
            resolver.update(uri, values, null, null)
        }
    }

    /**
     * 주어진 URL에서 이미지를 다운로드 받아 앱의 캐시 디렉토리에 임시 파일로 저장한 후, FileProvider를 사용하여 해당 파일의 URI를 반환하는 함수
     *
     * 이미지 다운로드, 임시 파일 저장, FileProvider를 통한 URI 반환 작업을 진행
     */
    suspend fun getCacheImageUri(id: Long, imageUrl: String) =
        runCatching {
            withContext(Dispatchers.IO) {
                val bitmap = downloadBitmapFromUrl(imageUrl) ?: throw Exception()
                val tempFile = createNamedCacheFile(id)
                FileOutputStream(tempFile).saveBitmapToFile(bitmap)
                FileProvider.getUriForFile(appContext, "kr.genti.android.fileprovider", tempFile)
            }
        }

    /**
     * 앱의 캐시 디렉토리에 임시 JPEG 이미지 파일을 생성하고, 해당 파일의 URI와 파일 이름을 반환하는 함수
     */
    suspend fun getTempImageFile(): Result<File> =
        runCatching {
            withContext(Dispatchers.IO) {
                createNamedCacheFile()
            }
        }

    /**
     * 주어진 이미지 URI를 사용하여 이미지 공유를 위한 인텐트를 생성하는 함수
     *
     * Intent.ACTION_SEND 액션을 설정하고, 이미지 URI를 EXTRA_STREAM에 추가하며, 읽기 권한을 부여한 후, 사용자에게 공유할 앱을 선택할 수 있도록 chooser 인텐트를 생성
     */
    fun getImageChooserIntent(uri: Uri): Intent =
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_STREAM, uri)
            type = "image/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            createChooser(this, "SHARE_IMAGE_CHOOSER")
        }

    /**
    Uri 객체의 파일 이름을 추출하는 확장 함수
     */
    fun Uri.getImageName(): String = getFileName(resolver).toString()


    private suspend fun downloadBitmapFromUrl(imageUrl: String): Bitmap? =
        coilImageLoader.execute(
            ImageRequest.Builder(appContext).data(imageUrl).build()
        ).image?.toBitmap()

    private fun OutputStream?.saveBitmapToFile(bitmap: Bitmap) {
        this?.use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        } ?: throw Exception()
    }

    private fun createNamedCacheFile(id: Long? = null): File =
        File(appContext.cacheDir, createGentiFileName(id)).apply {
            if (!exists()) createNewFile()
        }

    private fun createGentiFileName(id: Long?): String {
        val timestamp = dateTimeFormatter.format(LocalDateTime.now())
        return if (id != null) {
            "img_genti_${id}_${timestamp}.jpeg"
        } else {
            "img_genti_${timestamp}.jpeg"
        }
    }
}