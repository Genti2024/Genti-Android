package kr.genti.main

import kr.genti.domain.entity.response.GenerateStatusModel
import kr.genti.domain.entity.response.GenerateStatusModel.Companion.emptyGenerateStatusModel
import kr.genti.domain.enums.GenerateStatus

data class MainState(
    val currentGenerateStatus: GenerateStatus = GenerateStatus.EMPTY,
    val isNotificationReceived: Boolean = false,
    val serverUnableMessage: String = "",
    val generatedImage: GenerateStatusModel = emptyGenerateStatusModel(),
    val isFinishedDialogVisible: Boolean = false,
    val isErrorDialogVisible: Boolean = false,
    val isUnableDialogVisible: Boolean = false,
    val isSelectDialogVisible: Boolean = false,
    val isNetworkDialogVisible: Boolean = false
)