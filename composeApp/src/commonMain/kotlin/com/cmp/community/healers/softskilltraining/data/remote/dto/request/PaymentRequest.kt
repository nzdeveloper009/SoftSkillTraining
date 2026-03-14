package com.cmp.community.healers.softskilltraining.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InitiatePaymentRequest(
    @SerialName("amount") val amount: Int = 5000
)