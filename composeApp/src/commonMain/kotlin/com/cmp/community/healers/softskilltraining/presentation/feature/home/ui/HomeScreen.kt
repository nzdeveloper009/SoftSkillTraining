package com.cmp.community.healers.softskilltraining.presentation.feature.home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmp.community.healers.softskilltraining.data.repository.PlatformWebView
import com.cmp.community.healers.softskilltraining.domain.repository.WebViewNavigator
import com.cmp.community.healers.softskilltraining.presentation.feature.home.helper.NativeDestination
import com.cmp.community.healers.softskilltraining.presentation.feature.home.helper.PATH_TO_NATIVE
import com.cmp.community.healers.softskilltraining.theme.*
import com.cmp.community.healers.softskilltraining.utils.constants.HOME_URL


@Composable
fun HomeScreen(
    onNavigateBack: () -> Unit = {},
    onNavigateToSignIn: () -> Unit = {}            // ← Candidate Portal card tapped
) {
    var pageTitle  by remember { mutableStateOf("Community Healers") }
    var isLoading  by remember { mutableStateOf(true) }
    var refreshKey by remember { mutableStateOf(0) }
    var canGoBack  by remember { mutableStateOf(false) }
    var navigator  by remember { mutableStateOf<WebViewNavigator?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundWhite)
    ) {
        // ── App bar ───────────────────────────────────────────────────────────
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = 2.dp),
            color = BackgroundWhite
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(56.dp)
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (canGoBack) navigator?.goBack()
                        else onNavigateBack()
                    }
                ) {
                    Icon(Icons.Outlined.ArrowBack, contentDescription = "Back", tint = LabelColor)
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = pageTitle,
                        style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = LabelColor),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = HOME_URL,
                        style = TextStyle(fontSize = 11.sp, color = SubtitleColor),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = { refreshKey++ }) {
                    Icon(Icons.Outlined.Refresh, contentDescription = "Refresh", tint = PrimaryGreen)
                }
            }
        }

        // ── Loading indicator ─────────────────────────────────────────────────
        if (isLoading) {
            LinearProgressIndicator(
                modifier   = Modifier.fillMaxWidth(),
                color      = PrimaryGreen,
                trackColor = Color(0xFFD8F3DC)
            )
        }

        // ── WebView ───────────────────────────────────────────────────────────
        PlatformWebView(
            url        = HOME_URL,
            refreshKey = refreshKey,
            modifier   = Modifier
                .fillMaxWidth()
                .weight(1f),
            onPageStarted      = { isLoading = true },
            onPageFinished     = { title ->
                isLoading = false
                if (title.isNotBlank()) pageTitle = title
            },
            onNavigatorReady   = { navigator = it },
            onCanGoBackChanged = { canGoBack = it },

            // ── Map intercepted paths → native navigation ─────────────────────
            onDeepLinkIntercepted = { path ->
                when (PATH_TO_NATIVE[path]) {
                    NativeDestination.SignIn         -> onNavigateToSignIn()
                    NativeDestination.TrainingSignIn -> { /* TODO */ }
                    NativeDestination.WebOnly, null  -> { /* let WebView handle it */ }
                }
            }
        )
    }
}