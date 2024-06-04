package kr.genti.domain.repository

interface UploadRepository {
    suspend fun uploadImage(
        preSignedURL: String,
        imageUri: String,
    ): Result<Unit>
}
