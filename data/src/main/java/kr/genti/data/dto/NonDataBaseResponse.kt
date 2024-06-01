package kr.genti.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NonDataBaseResponse(
    @SerialName("success")
    val success: Boolean,
    @SerialName("errorCode")
    val errorCode: String?,
    @SerialName("errorMessage")
    val errorMessage: String?,
)
