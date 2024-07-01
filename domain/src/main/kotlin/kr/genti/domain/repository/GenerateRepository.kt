package kr.genti.domain.repository

import kr.genti.domain.entity.response.GenerateStatusModel

interface GenerateRepository {
    suspend fun getGenerateStatus(): Result<GenerateStatusModel>
}
