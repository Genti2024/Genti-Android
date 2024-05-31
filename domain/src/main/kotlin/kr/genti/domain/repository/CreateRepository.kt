package kr.genti.domain.repository

import kr.genti.domain.entity.response.PromptModel

interface CreateRepository {
    suspend fun getExamplePrompts(): Result<List<PromptModel>>
}
