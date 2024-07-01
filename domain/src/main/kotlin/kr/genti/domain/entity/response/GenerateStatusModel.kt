package kr.genti.domain.entity.response

import kr.genti.domain.enums.GenerateStatus

data class GenerateStatusModel(
    val pictureGenerateRequestId: Long,
    val status: GenerateStatus,
)
