package com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.document_upload.helper.CandidateTab
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.document_upload.helper.DocumentType

sealed interface CandidateHomeEvent : UiEvent {

    // Top bar
    data class TabChanged(val tab: CandidateTab)        : CandidateHomeEvent
    data object ToggleLanguage                          : CandidateHomeEvent
    data object Logout                                  : CandidateHomeEvent

    // Section toggles
    data object TogglePersonalSection                   : CandidateHomeEvent
    data object ToggleDocumentsSection                  : CandidateHomeEvent
    data object ToggleEducationSection                  : CandidateHomeEvent

    // Personal information
    data class FatherNameChanged(val value: String)     : CandidateHomeEvent
    data class CnicChanged(val value: String)           : CandidateHomeEvent
    data class DateOfBirthChanged(val value: String)    : CandidateHomeEvent
    data class CityChanged(val value: String)           : CandidateHomeEvent
    data class AddressChanged(val value: String)        : CandidateHomeEvent
    data object ToggleCityDropdown                      : CandidateHomeEvent

    // Documents
    /**
     * UI requests the ViewModel to open the file picker for [type].
     * ViewModel responds by emitting [CandidateHomeEffect.PickDocument].
     * The Screen collects that effect and calls [rememberFilePicker].
     */
    data class RequestPickDocument(val type: DocumentType) : CandidateHomeEvent

    /**
     * Called by the Screen's [rememberFilePicker] callback once the user
     * picks a file.  URI is already resolved.
     */
    data class DocumentSelected(val type: DocumentType, val uri: String) : CandidateHomeEvent

    // Education
    data object ToggleSixteenYearsEducation             : CandidateHomeEvent
    /**
     * UI requests the ViewModel to trigger degree picker.
     * ViewModel emits [CandidateHomeEffect.PickDegree].
     */
    data object RequestPickDegree                       : CandidateHomeEvent
    data class DegreeSelected(val uri: String)          : CandidateHomeEvent

    // Navigation
    data object ContinueToPayment                       : CandidateHomeEvent
}