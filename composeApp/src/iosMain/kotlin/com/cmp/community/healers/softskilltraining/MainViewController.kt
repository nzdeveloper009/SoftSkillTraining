package com.cmp.community.healers.softskilltraining

import androidx.compose.ui.window.ComposeUIViewController
import com.cmp.community.healers.softskilltraining.di.initKoin

private var koinInitialized = false

fun MainViewController() = ComposeUIViewController {
    if (!koinInitialized) {
        initKoin()
        koinInitialized = true
    }
    App()
}