package kr.genti.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("success")
    val success: Boolean,
    @SerialName("response")
    val response: T,
    @SerialName("errorCode")
    val errorCode: String?,
    @SerialName("errorMessage")
    val errorMessage: String?,
)
