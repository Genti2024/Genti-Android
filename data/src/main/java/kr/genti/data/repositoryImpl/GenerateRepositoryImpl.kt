package kr.genti.data.repositoryImpl

import kr.genti.data.dataSource.GenerateDataSource
import kr.genti.data.dto.request.ReportRequestDto.Companion.toDto
import kr.genti.domain.entity.request.ReportRequestModel
import kr.genti.domain.entity.response.GenerateStatusModel
import kr.genti.domain.entity.response.PicturePagedListModel
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

        override suspend fun getGeneratedPictureList(
            page: Int,
            size: Int,
            sortBy: String?,
            direction: String?,
        ): Result<PicturePagedListModel> =
            runCatching {
                generateDataSource.getGeneratedPictureList(
                    page,
                    size,
                    sortBy,
                    direction,
                ).response.toModel()
            }

        override suspend fun postGenerateReport(request: ReportRequestModel): Result<Boolean> =
            runCatching {
                generateDataSource.postGenerateReport(request.toDto()).response
            }
    }
