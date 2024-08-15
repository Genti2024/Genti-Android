package kr.genti.data.repositoryImpl

import kr.genti.data.dataSource.InfoDataSource
import kr.genti.data.dto.request.SignupRequestDto.Companion.toDto
import kr.genti.domain.entity.request.SignupRequestModel
import kr.genti.domain.entity.response.SignUpUserModel
import kr.genti.domain.repository.InfoRepository
import javax.inject.Inject

class InfoRepositoryImpl
    @Inject
    constructor(
        private val infoDataSource: InfoDataSource,
    ) : InfoRepository {
        override suspend fun postSignupData(request: SignupRequestModel): Result<SignUpUserModel> =
            runCatching {
                infoDataSource.postSignupData(request.toDto()).response.toModel()
            }

        override suspend fun postUserLogout(): Result<Boolean> =
            runCatching {
                infoDataSource.postUserLogout().response
            }

        override suspend fun deleteUser(): Result<Boolean> =
            runCatching {
                infoDataSource.deleteUser().response
            }
    }
