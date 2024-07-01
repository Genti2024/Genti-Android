package kr.genti.data.repositoryImpl

import kr.genti.data.dataSource.GenerateDataSource
import kr.genti.domain.entity.response.GenerateStatusModel
import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

class GenerateRepositoryImpl
    @Inject
    constructor(
        private val generateDataSource: GenerateDataSource,
    ) : GenerateRepository {
        override suspend fun getGenerateStatus(): Result<GenerateStatusModel> =
            runCatching {
                generateDataSource.getGenerateStatus().response.toModel()
            }
    }
