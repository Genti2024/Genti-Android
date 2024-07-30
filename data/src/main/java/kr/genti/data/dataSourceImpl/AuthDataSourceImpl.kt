package kr.genti.data.dataSourceImpl

import kr.genti.data.dataSource.AuthDataSource
import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.AuthRequestDto
import kr.genti.data.dto.request.ReissueRequestDto
import kr.genti.data.dto.response.AuthTokenDto
import kr.genti.data.dto.response.ReissueTokenDto
import kr.genti.data.service.AuthService
import javax.inject.Inject

data class AuthDataSourceImpl
    @Inject
    constructor(
        private val authService: AuthService,
    ) : AuthDataSource {
        override suspend fun postReissueTokens(request: ReissueRequestDto): BaseResponse<ReissueTokenDto> =
            authService.postReissueTokens(request)

        override suspend fun postOauthDataToGetToken(request: AuthRequestDto): BaseResponse<AuthTokenDto> =
            authService.postOauthDataToGetToken(request)
    }
