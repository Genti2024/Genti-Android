package kr.genti.main

import kr.genti.domain.entity.response.GenerateStatusModel
import kr.genti.domain.entity.response.GenerateStatusModel.Companion.emptyGenerateStatusModel
import kr.genti.domain.enums.GenerateStatus

data class MainState(
    val isNotificationReceived: Boolean = false,
    val generatedImage: GenerateStatusModel = emptyGenerateStatusModel(),
    val isFinishedDialogVisible: Boolean = false,
    val isErrorDialogVisible: Boolean = false,
    val isUnableDialogVisible: Boolean = false,
    val isSelectDialogVisible: Boolean = false,
)