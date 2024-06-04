package kr.genti.data.repositoryImpl

import android.content.Context
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import kr.genti.data.service.UploadService
import kr.genti.data.util.ContentUriRequestBody
import kr.genti.domain.repository.UploadRepository
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
        ): Result<Unit> =
            runCatching {
                uploadService.putS3Image(
                    preSignedURL,
                    ContentUriRequestBody(context, Uri.parse(imageUri)),
                )
            }
    }
