package com.cmp.community.healers.softskilltraining.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VerifyOtpRequest(
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("otp")         val otp: String
)