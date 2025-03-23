package kr.genti.domain.repository

import kr.genti.domain.entity.request.ReportRequestModel
import kr.genti.domain.entity.response.GenerateStatusModel
import kr.genti.domain.entity.response.OpenchatModel
import kr.genti.domain.entity.response.PicturePagedListModel
import kr.genti.domain.entity.response.ServerAvailableModel

interface GenerateRepository {
    suspend fun getGenerateStatus(): GenerateStatusModel

    suspend fun postGenerateReport(request: ReportRequestModel): Boolean

    suspend fun postGenerateRate(responseId: Int, star: Int): Boolean

    suspend fun postVerifyGenerateState(responseId: Int): Boolean

    suspend fun getCanceledToReset(requestId: String): Boolean

    suspend fun getGeneratedPictureList(page: Int, size: Int): PicturePagedListModel

    suspend fun getOpenchatData(): OpenchatModel

    suspend fun getIsUserVerified(): Boolean

    suspend fun getIsServerAvailable(): ServerAvailableModel

    suspend fun patchStatusInDevelop(): Boolean
}
