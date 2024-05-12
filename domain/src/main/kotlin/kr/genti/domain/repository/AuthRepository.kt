package kr.genti.domain.repository

import kr.genti.domain.entity.request.TokenRequestModel
import kr.genti.domain.entity.response.AuthTokenModel

interface AuthRepository {
    suspend fun postReissueTokens(
        authorization: String,
        request: TokenRequestModel,
    ): Result<AuthTokenModel>
}
