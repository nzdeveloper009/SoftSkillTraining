package com.cmp.community.healers.softskilltraining.data.repository

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.cmp.community.healers.softskilltraining.domain.repository.WebViewNavigator
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.ObjCSignatureOverride
import kotlinx.cinterop.cValue
import platform.CoreGraphics.CGRect
import platform.Foundation.NSURL
import platform.Foundation.NSURLRequest
import platform.WebKit.*
import platform.darwin.NSObject

@OptIn(ExperimentalForeignApi::class)
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
    var wkWebView by remember { mutableStateOf<WKWebView?>(null) }

    LaunchedEffect(wkWebView) {
        wkWebView?.let { wv ->
            onNavigatorReady(object : WebViewNavigator {
                override fun goBack() { wv.goBack() }
                override fun reload() { wv.reload() }
            })
        }
    }

    LaunchedEffect(refreshKey) {
        if (refreshKey > 0) wkWebView?.reload()
    }

    UIKitView(
        modifier = modifier,
        factory = {
            val config = WKWebViewConfiguration()

            // ✅ Fix: use cValue<CGRect>() instead of CGRectZero
            //    WKWebView requires CValue<CGRect>, not the raw CGRect struct.
            val webView = WKWebView(frame = cValue<CGRect>(), configuration = config)

            val delegate = WebViewDelegate(
                onStarted  = { onPageStarted(); onCanGoBackChanged(webView.canGoBack) },
                onFinished = { title ->
                    onPageFinished(title)
                    onCanGoBackChanged(webView.canGoBack)
                }
            )
            webView.navigationDelegate = delegate
            webView.UIDelegate         = delegate

            val nsUrl     = NSURL.URLWithString(url)!!
            val nsRequest = NSURLRequest.requestWithURL(nsUrl)
            webView.loadRequest(nsRequest)

            wkWebView = webView
            webView
        },
        update = { /* refreshKey reload handled by LaunchedEffect above */ }
    )
}

// ── Delegates ─────────────────────────────────────────────────────────────────

private class WebViewDelegate(
    private val onStarted:  () -> Unit,
    private val onFinished: (title: String) -> Unit
) : NSObject(), WKNavigationDelegateProtocol, WKUIDelegateProtocol {

    @ObjCSignatureOverride
    override fun webView(webView: WKWebView, didStartProvisionalNavigation: WKNavigation?) {
        onStarted()
    }

    @ObjCSignatureOverride
    override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
        onFinished(webView.title ?: "")
    }
}