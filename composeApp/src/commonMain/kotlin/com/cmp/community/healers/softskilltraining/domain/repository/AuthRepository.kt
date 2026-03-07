package com.cmp.community.healers.softskilltraining.domain.repository

import com.cmp.community.healers.softskilltraining.core.network.NetworkResult
import com.cmp.community.healers.softskilltraining.data.remote.dto.response.AuthTokens

interface AuthRepository {
    suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String
    ): NetworkResult<Unit>

    suspend fun verifyOtp(phoneNumber: String, otp: String): NetworkResult<AuthTokens>

    suspend fun login(phoneNumber: String, password: String): NetworkResult<AuthTokens>

    suspend fun logout(accessToken: String): NetworkResult<Unit>

    suspend fun refreshToken(refreshToken: String): NetworkResult<AuthTokens>
}