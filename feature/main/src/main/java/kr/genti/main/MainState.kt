package kr.genti.main

import kr.genti.domain.entity.response.GenerateStatusModel
import kr.genti.domain.entity.response.GenerateStatusModel.Companion.emptyGenerateStatusModel
import kr.genti.domain.enums.GenerateStatus

data class MainState(
    val currentStatus: GenerateStatus = GenerateStatus.EMPTY,
    val isNotificationReceived: Boolean = false,
    val generatedImage: GenerateStatusModel = emptyGenerateStatusModel(),
)