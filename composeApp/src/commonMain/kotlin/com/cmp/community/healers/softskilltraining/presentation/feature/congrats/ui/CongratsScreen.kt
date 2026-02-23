package com.cmp.community.healers.softskilltraining.presentation.feature.congrats.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cmp.community.healers.softskilltraining.presentation.feature.congrats.component.CelebrationCard
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingState
import com.cmp.community.healers.softskilltraining.presentation.feature.home.component.TopBar
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeViewModel
import com.cmp.community.healers.softskilltraining.theme.BgScreen

@Composable
fun CongratsScreen(state: SchedulingState, homeState: CandidateHomeState, candidateHomeVm: CandidateHomeViewModel, onLogout: () -> Unit, onGoToProfile: () -> Unit) {
    // Entrance animation controller
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visible = true }

    Column(modifier = Modifier.fillMaxSize().background(BgScreen)) {

        // ── Same TopBar ───────────────────────────────────────────────────────
        TopBar(
            state        = homeState,
            onTab        = { candidateHomeVm.onEvent(CandidateHomeEvent.TabChanged(it)) },
            onLangToggle = { candidateHomeVm.onEvent(CandidateHomeEvent.ToggleLanguage) },
            onLogout     = onLogout
        )

        // ── Scrollable content ────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 14.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Main celebration card
            AnimatedVisibility(
                visible = visible,
                enter   = fadeIn(tween(500)) + slideInVertically(tween(500)) { it / 4 }
            ) {
                CelebrationCard(state = state, onGoToProfile = onGoToProfile)
            }
        }
    }
}