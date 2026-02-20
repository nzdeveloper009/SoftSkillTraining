package com.cmp.community.healers.softskilltraining.data.repository

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cmp.community.healers.softskilltraining.domain.repository.WebViewNavigator

/**
 * Cross-platform WebView composable.
 *
 * @param url                   Initial URL to load.
 * @param refreshKey            Increment to trigger a reload.
 * @param modifier              Layout modifier.
 * @param onPageStarted         Called when a new page starts loading.
 * @param onPageFinished        Called with the page title once loading completes.
 * @param onNavigatorReady      Returns a [WebViewNavigator] for goBack()/reload().
 * @param onCanGoBackChanged    True when the WebView history stack is non-empty.
 * @param onDeepLinkIntercepted Called when a link's path matches a native route.
 *                              The path (e.g. "/candidate/auth") is passed back
 *                              so the caller can navigate natively.
 */
@Composable
expect fun PlatformWebView(
    url: String,
    refreshKey: Int,
    modifier: Modifier = Modifier,
    onPageStarted: () -> Unit = {},
    onPageFinished: (title: String) -> Unit = {},
    onNavigatorReady: (WebViewNavigator) -> Unit = {},
    onCanGoBackChanged: (Boolean) -> Unit = {},
    onDeepLinkIntercepted: (path: String) -> Unit = {}
)