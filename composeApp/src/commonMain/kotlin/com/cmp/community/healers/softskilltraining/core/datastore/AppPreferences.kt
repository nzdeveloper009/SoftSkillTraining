package com.cmp.community.healers.softskilltraining.core.datastore

import kotlinx.coroutines.flow.Flow

interface AppPreferences {

    // ── Auth session ─────────────────────────────────────────────────────────
    val accessToken:   Flow<String?>
    val refreshToken:  Flow<String?>
    val loggedInPhone: Flow<String?>

    // ── App preferences ──────────────────────────────────────────────────────
    val isDarkMode: Flow<Boolean>
    val language:   Flow<String>        // "en" | "ur"

    // ── Mutations ────────────────────────────────────────────────────────────
    suspend fun saveAuthSession(accessToken: String, refreshToken: String, phone: String)
    suspend fun clearAuthSession()
    suspend fun setDarkMode(isDark: Boolean)
    suspend fun setLanguage(lang: String)
}