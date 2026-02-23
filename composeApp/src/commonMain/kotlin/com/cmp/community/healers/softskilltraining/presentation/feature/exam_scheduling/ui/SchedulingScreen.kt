package com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.congrats.ui.CongratsScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.component.SchedulingContent
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingEffect
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeViewModel
import com.cmp.community.healers.softskilltraining.theme.BgScreen
import com.cmp.community.healers.softskilltraining.utils.constants.scheduling.SchedulingPhase

@Composable
fun SchedulingScreen(
    vm: SchedulingViewModel = viewModel { SchedulingViewModel() },
    candidateHomeVm: CandidateHomeViewModel,
    onLogout:            () -> Unit            = {},
    onBackToPayment:     () -> Unit            = {},
    onRegistrationDone:  () -> Unit            = {}   // after "Go to Profile" on congrats
) {
    val state     by vm.state.collectAsStateWithLifecycle()
    val homeState by candidateHomeVm.state.collectAsStateWithLifecycle()
    val snackbar   = remember { SnackbarHostState() }

    LaunchedEffect(vm) {
        vm.effect.collect { eff ->
            when (eff) {
                SchedulingEffect.NavigateBackToPayment -> onBackToPayment()
                SchedulingEffect.NavigateToProfile     -> onRegistrationDone()
                is SchedulingEffect.ShowSnackbar       -> snackbar.showSnackbar(eff.msg)
            }
        }
    }

    Scaffold(
        snackbarHost   = { SnackbarHost(snackbar) },
        containerColor = BgScreen
    ) { pad ->
        // When phase == COMPLETE, show the full-screen congrats overlay
        AnimatedContent(
            targetState = state.phase,
            transitionSpec = {
                if (targetState == SchedulingPhase.COMPLETE)
                    fadeIn(tween(400)) + slideInVertically { it / 6 } togetherWith
                            fadeOut(tween(200))
                else
                    fadeIn(tween(300)) togetherWith fadeOut(tween(200))
            },
            label = "phase",
            modifier = Modifier.fillMaxSize().padding(pad)
        ) { phase ->
            when (phase) {
                SchedulingPhase.COMPLETE -> {
                    CongratsScreen(
                        state    = state,
                        homeState = homeState,
                        candidateHomeVm = candidateHomeVm,
                        onLogout = onLogout,
                        onGoToProfile = { vm.onEvent(SchedulingEvent.GoToProfile) }
                    )
                }
                else -> {
                    SchedulingContent(
                        state           = state,
                        homeState       = homeState,
                        candidateHomeVm = candidateHomeVm,
                        onLogout        = onLogout,
                        onEvent         = { vm.onEvent(it) }
                    )
                }
            }
        }
    }
}