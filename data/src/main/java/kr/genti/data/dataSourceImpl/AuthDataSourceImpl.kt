package kr.genti.data.dataSourceImpl

import kr.genti.data.dataSource.AuthDataSource
import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.request.TokenRequestDto
import kr.genti.data.dto.response.AuthTokenDto
import kr.genti.data.service.AuthService
import javax.inject.Inject

data class AuthDataSourceImpl
    @Inject
    constructor(
        private val authService: AuthService,
    ) : AuthDataSource {
        override suspend fun postReissueTokens(
            authorization: String,
            request: TokenRequestDto,
        ): BaseResponse<AuthTokenDto> = authService.postReissueTokens(authorization, request)
    }
