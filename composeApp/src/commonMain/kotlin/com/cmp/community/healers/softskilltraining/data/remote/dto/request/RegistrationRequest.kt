package com.cmp.community.healers.softskilltraining.data.remote.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    @SerialName("firstName")   val firstName: String,
    @SerialName("lastName")    val lastName: String,
    @SerialName("email")       val email: String,
    @SerialName("phoneNumber") val phoneNumber: String,
    @SerialName("password")    val password: String
)