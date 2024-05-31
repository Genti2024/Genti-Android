package kr.genti.data.repositoryImpl

import kr.genti.data.dataSource.AuthDataSource
import kr.genti.data.dto.request.toTokenRequestModel
import kr.genti.domain.entity.request.TokenRequestModel
import kr.genti.domain.entity.response.AuthTokenModel
import kr.genti.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl
    @Inject
    constructor(
        private val authDataSource: AuthDataSource,
    ) : AuthRepository {
        override suspend fun postReissueTokens(
            authorization: String,
            request: TokenRequestModel,
        ): Result<AuthTokenModel> =
            runCatching {
                authDataSource.postReissueTokens(
                    authorization,
                    request.toTokenRequestModel(),
                ).response.toAuthTokenModel()
            }
    }
