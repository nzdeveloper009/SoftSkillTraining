package com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEffect
import com.cmp.community.healers.softskilltraining.utils.constants.document.DocumentType

sealed interface CandidateHomeEffect : UiEffect {

    // ── Auth ──────────────────────────────────────────────────────────────────
    data object NavigateToLogin                                  : CandidateHomeEffect

    // ── Application step navigation ───────────────────────────────────────────

    /** Navigate to / stay on registration screen (step 1) */
    data object NavigateToRegistration                           : CandidateHomeEffect

    /** Navigate to payment screen (step 2) */
    data object NavigateToPayment                                : CandidateHomeEffect

    /** Navigate to scheduling screen (step 3) */
    data object NavigateToScheduling                             : CandidateHomeEffect

    /** All steps complete — navigate to the "completed" home screen */
    data object NavigateToCandidateScheduledHome                 : CandidateHomeEffect

    // ── File pickers ──────────────────────────────────────────────────────────
    data class PickDocument(val type: DocumentType)              : CandidateHomeEffect
    data object PickDegree                                       : CandidateHomeEffect

    // ── Feedback ─────────────────────────────────────────────────────────────
    data class ShowSnackbar(val message: String)                 : CandidateHomeEffect
}