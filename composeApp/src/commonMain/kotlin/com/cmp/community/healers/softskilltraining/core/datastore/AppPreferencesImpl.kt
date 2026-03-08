package com.cmp.community.healers.softskilltraining.core.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppPreferencesImpl(
    private val dataStore: DataStore<Preferences>
) : AppPreferences {

    companion object {
        val KEY_ACCESS_TOKEN   = stringPreferencesKey("access_token")
        val KEY_REFRESH_TOKEN  = stringPreferencesKey("refresh_token")
        val KEY_LOGGED_IN_PHONE = stringPreferencesKey("logged_in_phone")
        val KEY_IS_DARK_MODE   = booleanPreferencesKey("is_dark_mode")
        val KEY_LANGUAGE       = stringPreferencesKey("app_language")
    }

    override val accessToken:   Flow<String?> = dataStore.data.map { it[KEY_ACCESS_TOKEN] }
    override val refreshToken:  Flow<String?> = dataStore.data.map { it[KEY_REFRESH_TOKEN] }
    override val loggedInPhone: Flow<String?> = dataStore.data.map { it[KEY_LOGGED_IN_PHONE] }
    override val isDarkMode:    Flow<Boolean> = dataStore.data.map { it[KEY_IS_DARK_MODE] ?: false }
    override val language:      Flow<String>  = dataStore.data.map { it[KEY_LANGUAGE] ?: "en" }

    override suspend fun saveAuthSession(accessToken: String, refreshToken: String, phone: String) {
        dataStore.edit { prefs ->
            prefs[KEY_ACCESS_TOKEN]    = accessToken
            prefs[KEY_REFRESH_TOKEN]   = refreshToken
            prefs[KEY_LOGGED_IN_PHONE] = phone
        }
    }

    override suspend fun clearAuthSession() {
        dataStore.edit { prefs ->
            prefs.remove(KEY_ACCESS_TOKEN)
            prefs.remove(KEY_REFRESH_TOKEN)
            prefs.remove(KEY_LOGGED_IN_PHONE)
        }
    }

    override suspend fun setDarkMode(isDark: Boolean) {
        dataStore.edit { it[KEY_IS_DARK_MODE] = isDark }
    }

    override suspend fun setLanguage(lang: String) {
        dataStore.edit { it[KEY_LANGUAGE] = lang }
    }
}