package com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEvent
import com.cmp.community.healers.softskilltraining.utils.constants.homee.CandidateTab
import com.cmp.community.healers.softskilltraining.utils.constants.document.DocumentType

sealed interface CandidateHomeEvent : UiEvent {

    // ── Top bar ───────────────────────────────────────────────────────────────
    data class TabChanged(val tab: CandidateTab)        : CandidateHomeEvent
    data object ToggleLanguage                          : CandidateHomeEvent
    data object Logout                                  : CandidateHomeEvent

    // ── Section toggles ───────────────────────────────────────────────────────
    data object TogglePersonalSection                   : CandidateHomeEvent
    data object ToggleDocumentsSection                  : CandidateHomeEvent
    data object ToggleEducationSection                  : CandidateHomeEvent

    // ── Personal information ──────────────────────────────────────────────────
    data class FatherNameChanged(val value: String)     : CandidateHomeEvent
    data class CnicChanged(val value: String)           : CandidateHomeEvent
    data class DateOfBirthChanged(val value: String)    : CandidateHomeEvent
    data class CityChanged(val value: String)           : CandidateHomeEvent
    data class AddressChanged(val value: String)        : CandidateHomeEvent
    data object ToggleCityDropdown                      : CandidateHomeEvent

    // ── Documents ─────────────────────────────────────────────────────────────
    data class RequestPickDocument(val type: DocumentType) : CandidateHomeEvent
    data class DocumentSelected(val type: DocumentType, val uri: String) : CandidateHomeEvent

    // ── Education ─────────────────────────────────────────────────────────────
    data object ToggleSixteenYearsEducation             : CandidateHomeEvent
    data object RequestPickDegree                       : CandidateHomeEvent
    data class DegreeSelected(val uri: String)          : CandidateHomeEvent

    // ── Navigation ────────────────────────────────────────────────────────────

    /** "Continue to Payment" — validates form then emits NavigateToPayment effect */
    data object ContinueToPayment                       : CandidateHomeEvent

    /**
     * Fired when user taps the Application tab from Profile screen.
     * ViewModel reads [applicationStep] and emits the correct navigation effect
     * to resume where the user left off.
     */
    data object NavigateToApplicationTab                : CandidateHomeEvent

    // ── Step advancement (called externally from nav graph after each step) ───

    /** Call after registration form submitted successfully */
    data object MarkRegistrationComplete                : CandidateHomeEvent

    /** Call after payment confirmed — records training fee as paid */
    data object MarkPaymentComplete                     : CandidateHomeEvent

    /**
     * Call after training scheduled — stores training details on profile.
     */
    data class MarkSchedulingComplete(
        val trainingDate:    String,
        val trainingTime:    String,
        val trainingCenter:  String,
        val trainingAddress: String,
        val trainingCity:    String
    ) : CandidateHomeEvent
}