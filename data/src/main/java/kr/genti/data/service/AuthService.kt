package kr.genti.data.service

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.AuthRequestDto
import kr.genti.data.dto.request.ReissueRequestDto
import kr.genti.data.dto.response.AuthTokenDto
import kr.genti.data.dto.response.ReissueTokenDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("api/v1/users/reissue")
    suspend fun postReissueTokens(
        @Header("Authorization") authorization: String,
        @Body request: ReissueRequestDto,
    ): BaseResponse<ReissueTokenDto>

    @POST("auth/v1/login/oauth2/token")
    suspend fun postOauthDataToGetToken(
        @Body request: AuthRequestDto,
    ): BaseResponse<AuthTokenDto>
}
