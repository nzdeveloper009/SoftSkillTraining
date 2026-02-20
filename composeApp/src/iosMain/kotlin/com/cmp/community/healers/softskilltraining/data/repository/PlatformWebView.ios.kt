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

private val INTERCEPTED_PATHS = listOf(
    "/candidate/auth",
    "/training/auth",
    "/center/auth",
    "/admin/auth",
    "/ministry/auth"
)

/**
 * Same JS as Android — attaches click listeners + MutationObserver.
 * Posts to the native message handler "nativeBridge" instead of
 * calling AndroidBridge.
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
                var path = href.split('?')[0].split('#')[0];
                var intercepted = ${INTERCEPTED_PATHS.joinToString(
    prefix = "[",
    postfix = "]"
) { "\"$it\"" }};
                if (intercepted.indexOf(path) !== -1) {
                    e.preventDefault();
                    e.stopPropagation();
                    window.webkit.messageHandlers.nativeBridge.postMessage({ path: path });
                }
            }, true);
        });
    }

    attachListeners();

    var observer = new MutationObserver(function() { attachListeners(); });
    observer.observe(document.body, { childList: true, subtree: true });
})();
""".trimIndent()

@OptIn(ExperimentalForeignApi::class)
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

            // ── WKUserContentController: JS bridge + injected script ──────────
            val contentController = WKUserContentController()

            // Message handler — called by window.webkit.messageHandlers.nativeBridge.postMessage()
            val messageHandler = NativeBridgeMessageHandler(
                interceptedPaths      = INTERCEPTED_PATHS,
                onDeepLinkIntercepted = onDeepLinkIntercepted
            )
            contentController.addScriptMessageHandler(messageHandler, name = "nativeBridge")

            // Inject the script at document-end so document.body exists
            val userScript = WKUserScript(
                source            = INTERCEPT_JS,
                injectionTime     = WKUserScriptInjectionTime.WKUserScriptInjectionTimeAtDocumentEnd,
                forMainFrameOnly  = false
            )
            contentController.addUserScript(userScript)
            config.userContentController = contentController

            val webView = WKWebView(frame = cValue<CGRect>(), configuration = config)

            val delegate = WebViewNavigationDelegate(
                interceptedPaths      = INTERCEPTED_PATHS,
                onStarted             = { onPageStarted(); onCanGoBackChanged(webView.canGoBack) },
                onFinished            = { title ->
                    onPageFinished(title)
                    onCanGoBackChanged(webView.canGoBack)
                },
                onDeepLinkIntercepted = onDeepLinkIntercepted
            )
            webView.navigationDelegate = delegate

            val nsUrl     = NSURL.URLWithString(url)!!
            val nsRequest = NSURLRequest.requestWithURL(nsUrl)
            webView.loadRequest(nsRequest)

            wkWebView = webView
            webView
        },
        update = { }
    )
}

// ── WKScriptMessageHandler — receives messages from JS bridge ─────────────────

private class NativeBridgeMessageHandler(
    private val interceptedPaths:      List<String>,
    private val onDeepLinkIntercepted: (String) -> Unit
) : NSObject(), WKScriptMessageHandlerProtocol {

    override fun userContentController(
        userContentController: WKUserContentController,
        didReceiveScriptMessage: WKScriptMessage
    ) {
        val body = didReceiveScriptMessage.body
        // Body is a JS object { path: "/candidate/auth" } → comes as Map
        @Suppress("UNCHECKED_CAST")
        val path = (body as? Map<String, Any>)?.get("path") as? String ?: return
        val intercepted = interceptedPaths.firstOrNull { path.startsWith(it) } ?: return
        onDeepLinkIntercepted(intercepted)
    }
}

// ── WKNavigationDelegate — fallback for hard URL navigations ──────────────────

private class WebViewNavigationDelegate(
    private val interceptedPaths:      List<String>,
    private val onStarted:             () -> Unit,
    private val onFinished:            (title: String) -> Unit,
    private val onDeepLinkIntercepted: (String) -> Unit
) : NSObject(), WKNavigationDelegateProtocol {

    @ObjCSignatureOverride
    override fun webView(webView: WKWebView, didStartProvisionalNavigation: WKNavigation?) {
        onStarted()
    }

    @ObjCSignatureOverride
    override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
        onFinished(webView.title ?: "")
    }

    // Fallback: intercept at URL level for non-JS navigations
    override fun webView(
        webView: WKWebView,
        decidePolicyForNavigationAction: WKNavigationAction,
        decisionHandler: (WKNavigationActionPolicy) -> Unit
    ) {
        val path = decidePolicyForNavigationAction.request.URL?.path ?: run {
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
            return
        }
        val intercepted = interceptedPaths.firstOrNull { path.startsWith(it) }
        if (intercepted != null) {
            onDeepLinkIntercepted(intercepted)
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyCancel)
        } else {
            decisionHandler(WKNavigationActionPolicy.WKNavigationActionPolicyAllow)
        }
    }
}