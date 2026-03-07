package com.cmp.community.healers.softskilltraining.core.network

import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

val httpClient: HttpClient = HttpClient {
    install(HttpTimeout) {
        requestTimeoutMillis = 90_000L   // 90 s — allows Render cold start
        connectTimeoutMillis = 30_000L
        socketTimeoutMillis  = 90_000L
    }
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
    install(Logging) {
        level = LogLevel.BODY
        logger = Logger.SIMPLE
    }
}