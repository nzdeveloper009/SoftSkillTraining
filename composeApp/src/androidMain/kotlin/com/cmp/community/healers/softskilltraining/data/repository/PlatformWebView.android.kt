package com.cmp.community.healers.softskilltraining.data.repository

import android.graphics.Bitmap
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.cmp.community.healers.softskilltraining.domain.repository.WebViewNavigator

// ── Paths that must open as native screens instead of loading in WebView ───────
private val INTERCEPTED_PATHS = listOf(
    "/candidate/auth",
    "/training/auth",
    "/center/auth",
    "/admin/auth",
    "/ministry/auth"
)

/**
 * JavaScript injected after every page load.
 *
 * Attaches a click listener to every <a> tag on the page.
 * When a link whose href matches an intercepted path is clicked:
 *   1. preventDefault() stops the Next.js router from handling it.
 *   2. AndroidBridge.onLinkClicked(path) fires the Kotlin callback.
 *
 * A MutationObserver re-runs the attachment whenever the DOM changes
 * (covers dynamically rendered SPA content).
 */
private val INTERCEPT_JS = """
(function() {
    function attachListeners() {
        document.querySelectorAll('a[href]').forEach(function(anchor) {
            if (anchor.__nativeIntercepted) return;
            anchor.__nativeIntercepted = true;
            anchor.addEventListener('click', function(e) {
                var href = anchor.getAttribute('href');
                if (!href) return;
                // Normalize: strip query/hash, keep just the path
                var path = href.split('?')[0].split('#')[0];
                var intercepted = ${INTERCEPTED_PATHS.joinToString(
    prefix = "[",
    postfix = "]"
) { "\"$it\"" }};
                if (intercepted.indexOf(path) !== -1) {
                    e.preventDefault();
                    e.stopPropagation();
                    AndroidBridge.onLinkClicked(path);
                }
            }, true);
        });
    }

    // Run immediately for server-rendered links
    attachListeners();

    // Re-run whenever new DOM nodes are added (Next.js hydration / SPA nav)
    var observer = new MutationObserver(function() { attachListeners(); });
    observer.observe(document.body, { childList: true, subtree: true });
})();
""".trimIndent()

@Composable
actual fun PlatformWebView(
    url: String,
    refreshKey: Int,
    modifier: Modifier,
    onPageStarted: () -> Unit,
    onPageFinished: (title: String) -> Unit,
    onNavigatorReady: (WebViewNavigator) -> Unit,
    onCanGoBackChanged: (Boolean) -> Unit,
    onDeepLinkIntercepted: (path: String) -> Unit
) {
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    LaunchedEffect(webViewRef) {
        webViewRef?.let { wv ->
            onNavigatorReady(object : WebViewNavigator {
                override fun goBack()  { wv.goBack() }
                override fun reload()  { wv.reload() }
            })
        }
    }

    LaunchedEffect(refreshKey) {
        if (refreshKey > 0) webViewRef?.reload()
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WebView(context).apply {

                settings.apply {
                    javaScriptEnabled                = true
                    domStorageEnabled                = true
                    databaseEnabled                  = true
                    loadWithOverviewMode             = true
                    useWideViewPort                  = true
                    builtInZoomControls              = false
                    displayZoomControls              = false
                    setSupportZoom(false)
                    cacheMode                        = WebSettings.LOAD_DEFAULT
                    mixedContentMode                 = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                    mediaPlaybackRequiresUserGesture = false
                    defaultTextEncodingName          = "utf-8"
                    allowFileAccess                  = true
                }

                val cookieManager = CookieManager.getInstance()
                cookieManager.setAcceptCookie(true)
                cookieManager.setAcceptThirdPartyCookies(this, true)

                // ── JavaScript bridge ─────────────────────────────────────────
                // AndroidBridge.onLinkClicked(path) is called from JS when an
                // intercepted link is clicked. Runs on the JS thread, so we
                // post to main thread before firing the Compose callback.
                addJavascriptInterface(
                    object {
                        @JavascriptInterface
                        fun onLinkClicked(path: String) {
                            android.os.Handler(android.os.Looper.getMainLooper()).post {
                                val intercepted = INTERCEPTED_PATHS.firstOrNull {
                                    path.startsWith(it)
                                }
                                if (intercepted != null) {
                                    onDeepLinkIntercepted(intercepted)
                                }
                            }
                        }
                    },
                    "AndroidBridge"   // ← window.AndroidBridge in JS
                )

                webViewClient = object : WebViewClient() {

                    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                        onPageStarted()
                        onCanGoBackChanged(view.canGoBack())
                    }

                    override fun onPageFinished(view: WebView, url: String) {
                        onPageFinished(view.title ?: "")
                        onCanGoBackChanged(view.canGoBack())
                        // Inject the click-intercept script after every page load
                        view.evaluateJavascript(INTERCEPT_JS, null)
                    }

                    // Still keep URL-level interception as a fallback for
                    // non-SPA navigations (hard reloads, direct URL changes)
                    override fun shouldOverrideUrlLoading(
                        view: WebView,
                        request: WebResourceRequest
                    ): Boolean {
                        val path = request.url.path ?: return false
                        val intercepted = INTERCEPTED_PATHS.firstOrNull { path.startsWith(it) }
                        if (intercepted != null) {
                            onDeepLinkIntercepted(intercepted)
                            return true
                        }
                        view.loadUrl(request.url.toString())
                        return true
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