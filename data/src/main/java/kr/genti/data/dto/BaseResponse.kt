package kr.genti.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("success")
    val success: Boolean,
    @SerialName("response")
    val response: T,
    @SerialName("code")
    val code: String,
    @SerialName("message")
    val message: String,
    @SerialName("status")
    val status: Int,
)
