package kr.genti.data.service

import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Url

interface UploadService {
    @PUT
    suspend fun putS3Image(
        @Url preSignedURL: String,
        @Body image: RequestBody,
    )
}
