package kr.genti.data.repositoryImpl

import kr.genti.data.dataSource.AuthDataSource
import kr.genti.data.dto.request.AuthRequestDto.Companion.toDto
import kr.genti.data.dto.request.ReissueRequestDto.Companion.toDto
import kr.genti.domain.entity.request.AuthRequestModel
import kr.genti.domain.entity.request.ReissueRequestModel
import kr.genti.domain.entity.response.AuthTokenModel
import kr.genti.domain.entity.response.ReissueTokenModel
import kr.genti.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl
@Inject
constructor(
    private val authDataSource: AuthDataSource,
) : AuthRepository {
    override suspend fun postReissueTokens(request: ReissueRequestModel): ReissueTokenModel =
        authDataSource.postReissueTokens(request.toDto()).response.toModel()

    override suspend fun postOauthDataToGetToken(request: AuthRequestModel): AuthTokenModel =
        authDataSource.postOauthDataToGetToken(request.toDto()).response.toModel()
}
