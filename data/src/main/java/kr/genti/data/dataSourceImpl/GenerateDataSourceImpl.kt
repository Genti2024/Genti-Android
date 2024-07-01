package kr.genti.data.dataSourceImpl

import kr.genti.data.dataSource.GenerateDataSource
import kr.genti.data.dto.BaseResponse
import kr.genti.data.dto.response.GenerateStatusDto
import kr.genti.data.service.GenerateService
import javax.inject.Inject

data class GenerateDataSourceImpl
    @Inject
    constructor(
        private val generateService: GenerateService,
    ) : GenerateDataSource {
        override suspend fun getGenerateStatus(): BaseResponse<GenerateStatusDto> = generateService.getGenerateStatus()
    }
