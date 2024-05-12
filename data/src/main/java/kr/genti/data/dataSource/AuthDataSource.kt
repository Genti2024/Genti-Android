package kr.genti.data.dataSource

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.TokenRequestDto
import kr.genti.data.dto.response.AuthTokenDto

interface AuthDataSource {
    suspend fun postReissueTokens(
        authorization: String,
        request: TokenRequestDto,
    ): BaseResponse<AuthTokenDto>
}
