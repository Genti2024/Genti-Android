package kr.genti.domain.entity.response

import kr.genti.domain.enums.GenerateStatus

data class GeneratePendingModel(
    val pictureGenerateRequestId: Long,
    val status: GenerateStatus,
)
