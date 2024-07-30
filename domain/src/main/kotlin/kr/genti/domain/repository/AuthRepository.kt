package kr.genti.domain.repository

import kr.genti.domain.entity.request.AuthRequestModel
import kr.genti.domain.entity.request.ReissueRequestModel
import kr.genti.domain.entity.response.AuthTokenModel
import kr.genti.domain.entity.response.ReissueTokenModel

interface AuthRepository {
    suspend fun postReissueTokens(request: ReissueRequestModel): Result<ReissueTokenModel>

    suspend fun postOauthDataToGetToken(request: AuthRequestModel): Result<AuthTokenModel>
}
