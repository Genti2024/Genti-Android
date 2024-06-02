package kr.genti.data.repositoryImpl

import kr.genti.data.dataSource.CreateDataSource
import kr.genti.domain.entity.response.PromptModel
import kr.genti.domain.repository.CreateRepository
import javax.inject.Inject

class CreateRepositoryImpl
    @Inject
    constructor(
        private val createDataSource: CreateDataSource,
    ) : CreateRepository {
        override suspend fun getExamplePrompts(): Result<List<PromptModel>> =
            runCatching {
                createDataSource.getExamplePrompts().response.map { it.toPromptModel() }
            }
    }
