package kr.genti.data.service

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.AuthRequestDto
import kr.genti.data.dto.request.ReissueRequestDto
import kr.genti.data.dto.response.AuthTokenDto
import kr.genti.data.dto.response.ReissueTokenDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("auth/v1/reissue")
    suspend fun postReissueTokens(
        @Body request: ReissueRequestDto,
    ): BaseResponse<ReissueTokenDto>

    @POST("auth/v1/login/oauth2/token/kakao")
    suspend fun postOauthDataToGetToken(
        @Body request: AuthRequestDto,
    ): BaseResponse<AuthTokenDto>
}
