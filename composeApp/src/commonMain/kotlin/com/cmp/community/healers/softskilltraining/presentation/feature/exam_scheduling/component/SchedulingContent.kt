package com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cmp.community.healers.softskilltraining.presentation.components.banner.WarningBanner
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingState
import com.cmp.community.healers.softskilltraining.presentation.feature.home.component.TopBar
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.profile.ui.ProfileScreen
import com.cmp.community.healers.softskilltraining.utils.constants.homee.CandidateTab
import com.cmp.community.healers.softskilltraining.utils.constants.scheduling.SchedulingPhase

@Composable
fun SchedulingContent(
    state:           SchedulingState,
    homeState:       com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState,
    candidateHomeVm: CandidateHomeViewModel,
    onLogout:        () -> Unit,
    onEvent:         (SchedulingEvent) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ── Shared TopBar ─────────────────────────────────────────────────
            TopBar(
                state        = homeState,
                onTab        = { candidateHomeVm.onEvent(CandidateHomeEvent.TabChanged(it)) },
                onLangToggle = { candidateHomeVm.onEvent(CandidateHomeEvent.ToggleLanguage) },
                onLogout     = onLogout
            )

            // ── Tab-aware body ────────────────────────────────────────────────
            // Profile tab → ProfileScreen inline (no nav push)
            // Application tab → scheduling content as before
            AnimatedContent(
                targetState  = homeState.activeTab,
                transitionSpec = { fadeIn(tween(220)) togetherWith fadeOut(tween(160)) },
                label        = "scheduling_tab"
            ) { tab ->
                when (tab) {
                    CandidateTab.PROFILE -> {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                        ) {
                            ProfileScreen(state = homeState)
                        }
                    }

                    CandidateTab.REGISTRATION -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            // ── Scrollable body ───────────────────────────────
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .verticalScroll(rememberScrollState())
                                    .padding(horizontal = 14.dp)
                                    .padding(top = 16.dp, bottom = 140.dp),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                SchedulingProgressCard()
                                SchedulingSectionHeader()
                                SelectTrainingDateHeader()
                                CalendarAndSelectionRow(state = state, onEvent = onEvent)

                                AnimatedVisibility(
                                    visible = state.phase == SchedulingPhase.SELECT_DATE,
                                    enter   = fadeIn(tween(300)),
                                    exit    = fadeOut(tween(200))
                                ) {
                                    WarningBanner("Please select a training date to complete your scheduling")
                                }
                            }

                            // ── Sticky bottom bar ─────────────────────────────
                            SchedulingBottomBar(
                                dateSelected = state.phase == SchedulingPhase.DATE_SELECTED,
                                isScheduling = state.isScheduling,
                                modifier     = Modifier.align(Alignment.BottomCenter),
                                onBack       = { onEvent(SchedulingEvent.BackToPayment) },
                                onContinue   = { onEvent(SchedulingEvent.CompleteRegistration) }
                            )
                        }
                    }
                }
            }
        }
    }
}

