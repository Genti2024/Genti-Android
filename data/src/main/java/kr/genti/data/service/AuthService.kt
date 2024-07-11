package kr.genti.data.service

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.TokenRequestDto
import kr.genti.data.dto.response.AuthTokenDto
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    // TODO: 서버 나오면 수정
    @POST("api/v1/users/reissue")
    suspend fun postReissueTokens(
        @Header("Authorization") authorization: String,
        @Body request: TokenRequestDto,
    ): BaseResponse<AuthTokenDto>
}
