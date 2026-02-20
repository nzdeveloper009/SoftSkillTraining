package com.cmp.community.healers.softskilltraining.domain.repository

/** Handle returned from the platform WebView to control navigation. */
interface WebViewNavigator {
    fun goBack()
    fun reload()
}