package kr.genti.data.repositoryImpl

import kr.genti.data.dataSource.InfoDataSource
import kr.genti.data.dto.request.SignupRequestDto.Companion.toDto
import kr.genti.domain.entity.request.SignupRequestModel
import kr.genti.domain.repository.InfoRepository
import javax.inject.Inject

class InfoRepositoryImpl
    @Inject
    constructor(
        private val infoDataSource: InfoDataSource,
    ) : InfoRepository {
        override suspend fun postSignupData(request: SignupRequestModel): Result<Boolean> =
            runCatching {
                infoDataSource.postSignupData(request.toDto()).response
            }
    }
