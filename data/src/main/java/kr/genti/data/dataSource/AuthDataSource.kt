package kr.genti.data.dataSource

import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.AuthRequestDto
import kr.genti.data.dto.request.ReissueRequestDto
import kr.genti.data.dto.response.AuthTokenDto
import kr.genti.data.dto.response.ReissueTokenDto

interface AuthDataSource {
    suspend fun postReissueTokens(request: ReissueRequestDto): BaseResponse<ReissueTokenDto>

    suspend fun postOauthDataToGetToken(request: AuthRequestDto): BaseResponse<AuthTokenDto>
}
