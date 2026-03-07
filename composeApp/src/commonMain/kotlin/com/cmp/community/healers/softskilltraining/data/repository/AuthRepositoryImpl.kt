package com.cmp.community.healers.softskilltraining.data.repository

import com.cmp.community.healers.softskilltraining.core.network.NetworkResult
import com.cmp.community.healers.softskilltraining.data.remote.api.AuthApi
import com.cmp.community.healers.softskilltraining.data.remote.dto.request.LoginRequest
import com.cmp.community.healers.softskilltraining.data.remote.dto.request.SignUpRequest
import com.cmp.community.healers.softskilltraining.data.remote.dto.request.VerifyOtpRequest
import com.cmp.community.healers.softskilltraining.data.remote.dto.response.AuthTokens
import com.cmp.community.healers.softskilltraining.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun signUp(
        firstName: String,
        lastName: String,
        email: String,
        phoneNumber: String,
        password: String
    ): NetworkResult<Unit> = api.signUp(
        SignUpRequest(firstName, lastName, email, phoneNumber, password)
    )

    override suspend fun verifyOtp(
        phoneNumber: String,
        otp: String
    ): NetworkResult<AuthTokens> = api.verifyOtp(VerifyOtpRequest(phoneNumber, otp))

    override suspend fun login(
        phoneNumber: String,
        password: String
    ): NetworkResult<AuthTokens> = api.login(LoginRequest(phoneNumber, password))

    override suspend fun logout(
        accessToken: String
    ): NetworkResult<Unit> = api.logout(accessToken)

    override suspend fun refreshToken(
        refreshToken: String
    ): NetworkResult<AuthTokens> = api.refreshToken(refreshToken)
}