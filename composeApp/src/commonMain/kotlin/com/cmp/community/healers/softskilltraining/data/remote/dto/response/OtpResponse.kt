package com.cmp.community.healers.softskilltraining.data.remote.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Tokens returned in verify-OTP and login responses
@Serializable
data class AuthTokens(
    @SerialName("accessToken")  val accessToken: String,
    @SerialName("refreshToken") val refreshToken: String
)

// Inner "data" wrapper: { statusCode, message, data: AuthTokens }
@Serializable
data class AuthDataWrapper(
    @SerialName("statusCode") val statusCode: Int,
    @SerialName("message")    val message: String,
    @SerialName("data")       val data: AuthTokens
)

// Outer envelope returned by /auth/signup/verify and /auth/login/candidate
@Serializable
data class AuthResponse(
    @SerialName("statusCode") val statusCode: Int,
    @SerialName("data")       val data: AuthDataWrapper,
    @SerialName("message")    val message: String,
    @SerialName("error")      val error: String? = null
)