package com.cmp.community.healers.softskilltraining.presentation.feature.payment.ui

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cmp.community.healers.softskilltraining.data.platform.saveReceiptToFile
import com.cmp.community.healers.softskilltraining.presentation.components.banner.SuccessBanner
import com.cmp.community.healers.softskilltraining.presentation.components.banner.WarningBanner
import com.cmp.community.healers.softskilltraining.presentation.feature.home.component.TopBar
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.component.PaymentBottomBar
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.component.PaymentProgressCard
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.component.PaymentSectionHeader
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.component.RegistrationFeeCard
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentEffect
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.document_upload.helper.CandidateTab
import com.cmp.community.healers.softskilltraining.theme.BgScreen
import com.cmp.community.healers.softskilltraining.utils.constants.payment.PaymentPhase

@Composable
fun PaymentScreen(
    vm:                    PaymentViewModel        = viewModel { PaymentViewModel() },
    // ✅ Same VM instance passed from AppNavGraph — shares tab/language state
    candidateHomeVm:       CandidateHomeViewModel,
    onLogout:              () -> Unit              = {},
    onBackToRegistration:  () -> Unit              = {},
    onContinueToScheduling:() -> Unit              = {}
) {
    val state         by vm.state.collectAsStateWithLifecycle()
    // ✅ Observe shared state so TopBar reacts to tab/language changes live
    val homeState     by candidateHomeVm.state.collectAsStateWithLifecycle()
    val snackbar       = remember { SnackbarHostState() }

    // ── Effects ───────────────────────────────────────────────────────────────
    LaunchedEffect(vm) {
        vm.effect.collect { eff ->
            when (eff) {
                PaymentEffect.NavigateBackToRegistration -> onBackToRegistration()
                PaymentEffect.NavigateToScheduling       -> onContinueToScheduling()
                is PaymentEffect.TriggerReceiptDownload  -> {
                    saveReceiptToFile(
                        fileName = "receipt_${eff.transactionId.replace("#", "")}",
                        content  = eff.receiptText
                    ) { /* ViewModel emits ShowSnackbar after this */ }
                }
                is PaymentEffect.ShowSnackbar -> snackbar.showSnackbar(eff.message)
            }
        }
    }

    Scaffold(
        snackbarHost   = { SnackbarHost(snackbar) },
        containerColor = BgScreen
    ) { pad ->
        Box(modifier = Modifier.fillMaxSize().padding(pad)) {

            Column(modifier = Modifier.fillMaxSize()) {

                // ✅ Reuse the EXACT same TopBar composable from CandidateHomeScreen.
                // homeState carries the live tab + language — identical appearance.
                // Tab/language events go back to candidateHomeVm so pressing Back
                // shows the same selection the user left on.
                TopBar(
                    state        = homeState,
                    onTab        = { candidateHomeVm.onEvent(CandidateHomeEvent.TabChanged(it)) },
                    onLangToggle = { candidateHomeVm.onEvent(CandidateHomeEvent.ToggleLanguage) },
                    onLogout     = onLogout
                )

                // ── Scrollable body ───────────────────────────────────────────
                // bottom = 140.dp — enough room for the 2-row bottom bar
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 14.dp)
                        .padding(top = 16.dp, bottom = 140.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    PaymentProgressCard()
                    PaymentSectionHeader()
                    RegistrationFeeCard(state = state, onEvent = { vm.onEvent(it) })

                    AnimatedContent(
                        targetState = state.phase,
                        transitionSpec = { fadeIn(tween(300)) togetherWith fadeOut(tween(200)) },
                        label = "banner"
                    ) { phase ->
                        when (phase) {
                            PaymentPhase.PAID -> SuccessBanner()
                            else              -> WarningBanner()
                        }
                    }
                }
            }

            // ── Sticky bottom bar ─────────────────────────────────────────────
            PaymentBottomBar(
                isPaid     = state.phase == PaymentPhase.PAID,
                modifier   = Modifier.align(Alignment.BottomCenter),
                onBack     = { vm.onEvent(PaymentEvent.BackToRegistration) },
                onContinue = { vm.onEvent(PaymentEvent.ContinueToScheduling) }
            )
        }
    }
}
