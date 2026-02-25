package com.cmp.community.healers.softskilltraining.presentation.feature.home.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cmp.community.healers.softskilltraining.presentation.feature.home.component.ScheduledCard
import com.cmp.community.healers.softskilltraining.presentation.feature.home.component.TopBar
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeEffect
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.profile.ui.ProfileScreen
import com.cmp.community.healers.softskilltraining.theme.BgScreen
import com.cmp.community.healers.softskilltraining.utils.constants.homee.CandidateTab

@Composable
fun CandidateScheduledHomeScreen(
    candidateHomeVm: CandidateHomeViewModel,
    onLogout:        () -> Unit = {}
) {
    val state by candidateHomeVm.state.collectAsStateWithLifecycle()

    LaunchedEffect(candidateHomeVm) {
        candidateHomeVm.effect.collect { effect ->
            when (effect) {
                CandidateHomeEffect.NavigateToLogin -> onLogout()
                else -> Unit
            }
        }
    }

    Scaffold(containerColor = BgScreen) { pad ->
        Column(modifier = Modifier.fillMaxSize().padding(pad)) {

            TopBar(
                state        = state,
                onTab        = { candidateHomeVm.onEvent(CandidateHomeEvent.TabChanged(it)) },
                onLangToggle = { candidateHomeVm.onEvent(CandidateHomeEvent.ToggleLanguage) },
                onLogout     = { candidateHomeVm.onEvent(CandidateHomeEvent.Logout) }
            )

            // Crossfade: Application tab → card   |   Profile tab → ProfileScreen
            AnimatedContent(
                targetState  = state.activeTab,
                transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
                label        = "scheduled_tab"
            ) { tab ->
                when (tab) {

                    CandidateTab.PROFILE -> {
                        // Inline profile — no nav push needed
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            ProfileScreen(state = state)
                        }
                    }

                    CandidateTab.REGISTRATION -> {
                        // "Training Already Scheduled" card
                        Box(
                            modifier         = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            ScheduledCard(
                                modifier = Modifier
                                    .padding(horizontal = 24.dp)
                                    .padding(top = 24.dp),
                                onGoToProfile = {
                                    // Tap "Go to Profile" → switch tab inline
                                    candidateHomeVm.onEvent(
                                        CandidateHomeEvent.TabChanged(CandidateTab.PROFILE)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}