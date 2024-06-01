package kr.genti.data.dataSourceImpl

import kr.genti.data.dataSource.CreateDataSource
import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.response.CreatePromptDto
import kr.genti.data.service.CreateService
import javax.inject.Inject

data class CreateDataSourceImpl
    @Inject
    constructor(
        private val createService: CreateService,
    ) : CreateDataSource {
        override suspend fun getExamplePrompts(): BaseResponse<List<CreatePromptDto>> = createService.getExamplePrompts()
    }
