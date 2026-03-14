package com.cmp.community.healers.softskilltraining

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.cmp.community.healers.softskilltraining.core.datastore.AppPreferences
import com.cmp.community.healers.softskilltraining.core.navigation.AppNavGraph
import com.cmp.community.healers.softskilltraining.theme.LocalAppStrings
import com.cmp.community.healers.softskilltraining.theme.englishStrings
import com.cmp.community.healers.softskilltraining.theme.urduStrings
import org.koin.compose.KoinContext
import org.koin.compose.koinInject

@Composable
fun App() {
    KoinContext {
        val appPreferences: AppPreferences = koinInject()
        val language by appPreferences.language.collectAsState("en")

        val strings    = if (language == "ur") urduStrings else englishStrings
        val layoutDir  = if (language == "ur") LayoutDirection.Rtl else LayoutDirection.Ltr

        CompositionLocalProvider(
            LocalAppStrings   provides strings,
            LocalLayoutDirection provides layoutDir
        ) {
            MaterialTheme {
                AppNavGraph()
            }
        }
    }
}