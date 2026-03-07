package com.cmp.community.healers.softskilltraining.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Response from /auth/signup/request — just confirms OTP was sent
@Serializable
data class SignUpRequestResponse(
    @SerialName("statusCode") val statusCode: Int,
    @SerialName("message")    val message: String,
    @SerialName("error")      val error: String? = null
)