package kr.genti.data.repositoryImpl

import kr.genti.data.dataSource.GenerateDataSource
import kr.genti.data.dto.request.ReportRequestDto.Companion.toDto
import kr.genti.domain.entity.request.ReportRequestModel
import kr.genti.domain.entity.response.GenerateStatusModel
import kr.genti.domain.entity.response.OpenchatModel
import kr.genti.domain.entity.response.PicturePagedListModel
import kr.genti.domain.entity.response.ServerAvailableModel
import kr.genti.domain.repository.GenerateRepository
import javax.inject.Inject

class GenerateRepositoryImpl
@Inject
constructor(
    private val generateDataSource: GenerateDataSource,
) : GenerateRepository {
    override suspend fun getGenerateStatus(): GenerateStatusModel =
        generateDataSource.getGenerateStatus().response.toModel()

    override suspend fun postGenerateReport(request: ReportRequestModel): Boolean =
        generateDataSource.postGenerateReport(request.toDto()).response

    override suspend fun postGenerateRate(responseId: Int, star: Int): Boolean =
        generateDataSource.postGenerateRate(responseId, star).response

    override suspend fun postVerifyGenerateState(responseId: Int): Boolean =
        generateDataSource.postVerifyGenerateState(responseId).response

    override suspend fun getCanceledToReset(requestId: String): Boolean =
        generateDataSource.getCanceledToReset(requestId).response

    override suspend fun getGeneratedPictureList(page: Int, size: Int): PicturePagedListModel =
        generateDataSource.getGeneratedPictureList(page, size).response.toModel()

    override suspend fun getOpenchatData(): OpenchatModel =
        generateDataSource.getOpenchatData().response.toModel()

    override suspend fun getIsUserVerified(): Boolean =
        generateDataSource.getIsUserVerified().response

    override suspend fun getIsServerAvailable(): ServerAvailableModel =
        generateDataSource.getIsServerAvailable().response.toModel()

    override suspend fun patchStatusInDevelop(): Boolean =
        generateDataSource.patchStatusInDevelop().response
}
