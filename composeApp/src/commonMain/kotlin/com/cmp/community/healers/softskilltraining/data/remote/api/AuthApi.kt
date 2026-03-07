package com.cmp.community.healers.softskilltraining.data.remote.api

import com.cmp.community.healers.softskilltraining.core.network.ApiConstants
import com.cmp.community.healers.softskilltraining.core.network.NetworkResult
import com.cmp.community.healers.softskilltraining.core.network.httpClient
import com.cmp.community.healers.softskilltraining.data.remote.dto.request.LoginRequest
import com.cmp.community.healers.softskilltraining.data.remote.dto.request.RefreshTokenRequest
import com.cmp.community.healers.softskilltraining.data.remote.dto.request.SignUpRequest
import com.cmp.community.healers.softskilltraining.data.remote.dto.request.VerifyOtpRequest
import com.cmp.community.healers.softskilltraining.data.remote.dto.response.AuthResponse
import com.cmp.community.healers.softskilltraining.data.remote.dto.response.AuthTokens
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

class AuthApi(private val client: HttpClient = httpClient) {

    // ── Sign up — request OTP ────────────────────────────────────────────────
    suspend fun signUp(request: SignUpRequest): NetworkResult<Unit> = safeCall {
        val response = client.post(url(ApiConstants.Endpoints.SIGNUP_REQUEST)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (response.status.isSuccess()) NetworkResult.Success(Unit)
        else NetworkResult.Error(parseError(response.bodyAsText()), response.status.value)
    }

    // ── Sign up — verify OTP and get tokens ──────────────────────────────────
    suspend fun verifyOtp(request: VerifyOtpRequest): NetworkResult<AuthTokens> = safeCall {
        val response = client.post(url(ApiConstants.Endpoints.SIGNUP_VERIFY)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (response.status.isSuccess()) {
            NetworkResult.Success(response.body<AuthResponse>().data.data)
        } else {
            NetworkResult.Error(parseError(response.bodyAsText()), response.status.value)
        }
    }

    // ── Login ────────────────────────────────────────────────────────────────
    suspend fun login(request: LoginRequest): NetworkResult<AuthTokens> = safeCall {
        val response = client.post(url(ApiConstants.Endpoints.LOGIN_CANDIDATE)) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
        if (response.status.isSuccess()) {
            NetworkResult.Success(response.body<AuthResponse>().data.data)
        } else {
            NetworkResult.Error(parseError(response.bodyAsText()), response.status.value)
        }
    }

    // ── Logout ───────────────────────────────────────────────────────────────
    suspend fun logout(accessToken: String): NetworkResult<Unit> = safeCall {
        val response = client.post(url(ApiConstants.Endpoints.LOGOUT)) {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }
        if (response.status.isSuccess()) NetworkResult.Success(Unit)
        else NetworkResult.Error(parseError(response.bodyAsText()), response.status.value)
    }

    // ── Refresh token ────────────────────────────────────────────────────────
    suspend fun refreshToken(refreshToken: String): NetworkResult<AuthTokens> = safeCall {
        val response = client.post(url(ApiConstants.Endpoints.REFRESH_TOKEN)) {
            contentType(ContentType.Application.Json)
            setBody(RefreshTokenRequest(refreshToken))
        }
        if (response.status.isSuccess()) {
            NetworkResult.Success(response.body<AuthResponse>().data.data)
        } else {
            NetworkResult.Error(parseError(response.bodyAsText()), response.status.value)
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private fun url(endpoint: String) = ApiConstants.BASE_URL + endpoint

    private fun parseError(body: String): String = try {
        val json = Json.parseToJsonElement(body).jsonObject
        json["message"]?.jsonPrimitive?.content
            ?: json["error"]?.jsonPrimitive?.content
            ?: body.take(200).ifBlank { "Something went wrong." }
    } catch (e: Exception) {
        body.take(200).ifBlank { "Something went wrong." }
    }

    private suspend fun <T> safeCall(block: suspend () -> NetworkResult<T>): NetworkResult<T> =
        try { block() }
        catch (e: HttpRequestTimeoutException) {
            NetworkResult.Error("Request timed out. The server may be waking up — please try again in a moment.")
        }
        catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Network error. Please check your connection.")
        }
}