package com.cmp.community.healers.softskilltraining.data.repository

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cmp.community.healers.softskilltraining.domain.repository.WebViewNavigator

/**
 * Cross-platform WebView composable.
 *
 * @param url               The initial URL to load.
 * @param refreshKey        Increment this to trigger a reload.
 * @param modifier          Compose layout modifier.
 * @param onPageStarted     Called when a new page starts loading.
 * @param onPageFinished    Called with the page title once loading completes.
 * @param onNavigatorReady  Returns a [WebViewNavigator] to call goBack()/reload().
 * @param onCanGoBackChanged Returns true when the WebView history has entries to go back to.
 */
@Composable
expect fun PlatformWebView(
    url: String,
    refreshKey: Int,
    modifier: Modifier = Modifier,
    onPageStarted: () -> Unit = {},
    onPageFinished: (title: String) -> Unit = {},
    onNavigatorReady: (WebViewNavigator) -> Unit = {},
    onCanGoBackChanged: (Boolean) -> Unit = {}
)