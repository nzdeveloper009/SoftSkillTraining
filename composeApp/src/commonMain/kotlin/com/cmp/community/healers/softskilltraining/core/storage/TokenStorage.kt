package com.cmp.community.healers.softskilltraining.core.storage

object TokenStorage {
    var accessToken: String? = null
    var refreshToken: String? = null

    fun save(accessToken: String, refreshToken: String) {
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    fun clear() {
        accessToken = null
        refreshToken = null
    }
}