package com.cmp.community.healers.softskilltraining.data.repository

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cmp.community.healers.softskilltraining.domain.repository.WebViewNavigator
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView

@Composable
actual fun PlatformWebView(
    url: String,
    refreshKey: Int,
    modifier: Modifier,
    onPageStarted: () -> Unit,
    onPageFinished: (title: String) -> Unit,
    onNavigatorReady: (WebViewNavigator) -> Unit,
    onCanGoBackChanged: (Boolean) -> Unit
) {
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    // Expose navigator as soon as we have the WebView reference
    LaunchedEffect(webViewRef) {
        webViewRef?.let { wv ->
            onNavigatorReady(object : WebViewNavigator {
                override fun goBack() { wv.goBack() }
                override fun reload() { wv.reload() }
            })
        }
    }

    // Reload when refreshKey changes (user tapped the refresh button)
    LaunchedEffect(refreshKey) {
        if (refreshKey > 0) webViewRef?.reload()
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {
                settings.apply {
                    javaScriptEnabled      = true
                    domStorageEnabled      = true
                    loadWithOverviewMode   = true
                    useWideViewPort        = true
                    builtInZoomControls    = false
                    displayZoomControls    = false
                    setSupportZoom(false)
                }

                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                        onPageStarted()
                        onCanGoBackChanged(view.canGoBack())
                    }
                    override fun onPageFinished(view: WebView, url: String) {
                        onPageFinished(view.title ?: "")
                        onCanGoBackChanged(view.canGoBack())
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onReceivedTitle(view: WebView, title: String) {
                        onPageFinished(title)
                    }
                }

                loadUrl(url)
                webViewRef = this
            }
        }
    )
}