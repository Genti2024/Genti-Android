package kr.genti.data.repositoryImpl

import android.content.Context
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import kr.genti.data.service.UploadService
import kr.genti.domain.repository.UploadRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class UploadRepositoryImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val uploadService: UploadService,
    ) : UploadRepository {
        override suspend fun uploadImage(
            preSignedURL: String,
            imageUri: String,
        ): Result<Int?> =
            runCatching {
                uploadService.putS3Image(
                    preSignedURL,
                    context.contentResolver.openInputStream(
                        imageUri.toUri(),
                    )?.readBytes()?.toRequestBody(OCTET_STREAM.toMediaType()),
                )
            }

        companion object {
            private const val OCTET_STREAM = "application/octet-stream"
        }
    }
