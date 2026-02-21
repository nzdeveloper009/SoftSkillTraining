package com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi

import androidx.lifecycle.viewModelScope
import com.cmp.community.healers.softskilltraining.core.base.BaseViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.document_upload.helper.DocumentType
import com.cmp.community.healers.softskilltraining.theme.AppLanguage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CandidateHomeViewModel(
    loggedInPhone: String = ""
) : BaseViewModel<CandidateHomeState, CandidateHomeEvent, CandidateHomeEffect>(
    CandidateHomeState(contactNumber = loggedInPhone)
) {
    override fun handleEvent(event: CandidateHomeEvent) {
        when (event) {

            // ── Top bar ───────────────────────────────────────────────────────
            is CandidateHomeEvent.TabChanged ->
                setState { copy(activeTab = event.tab) }

            CandidateHomeEvent.ToggleLanguage ->
                setState { copy(language = if (language == AppLanguage.ENGLISH) AppLanguage.URDU else AppLanguage.ENGLISH) }

            CandidateHomeEvent.Logout ->
                setEffect(CandidateHomeEffect.NavigateToLogin)

            // ── Section expand/collapse ───────────────────────────────────────
            CandidateHomeEvent.TogglePersonalSection  -> setState { copy(personalExpanded  = !personalExpanded) }
            CandidateHomeEvent.ToggleDocumentsSection -> setState { copy(documentsExpanded = !documentsExpanded) }
            CandidateHomeEvent.ToggleEducationSection -> setState { copy(educationExpanded = !educationExpanded) }

            // ── Personal information ──────────────────────────────────────────
            is CandidateHomeEvent.FatherNameChanged ->
                setState { copy(fatherName  = event.value, errors = errors - "fatherName") }

            is CandidateHomeEvent.CnicChanged ->
                setState { copy(cnicNumber  = event.value, errors = errors - "cnic") }

            is CandidateHomeEvent.DateOfBirthChanged ->
                setState { copy(dateOfBirth = event.value, errors = errors - "dob") }

            is CandidateHomeEvent.CityChanged ->
                setState { copy(city = event.value, cityDropdownOpen = false, errors = errors - "city") }

            is CandidateHomeEvent.AddressChanged ->
                setState { copy(address = event.value, errors = errors - "address") }

            CandidateHomeEvent.ToggleCityDropdown ->
                setState { copy(cityDropdownOpen = !cityDropdownOpen) }

            // ── Document upload ───────────────────────────────────────────────
            //
            // The Screen cannot call rememberFilePicker inside a LaunchedEffect
            // (it is a @Composable).  Instead:
            //   1. UI fires RequestPickDocument  →  ViewModel emits PickDocument effect
            //   2. Screen collects the effect    →  calls the pre-built launcher lambda
            //   3. User picks file               →  Screen fires DocumentSelected event
            //   4. ViewModel stores the URI in state

            is CandidateHomeEvent.RequestPickDocument ->
                setEffect(CandidateHomeEffect.PickDocument(event.type))

            is CandidateHomeEvent.DocumentSelected ->
                setState { copy(uploadedDocs = uploadedDocs + (event.type to event.uri), errors = errors - "docs") }

            // ── Education ─────────────────────────────────────────────────────
            CandidateHomeEvent.ToggleSixteenYearsEducation ->
                setState { copy(hasSixteenYearsEducation = !hasSixteenYearsEducation, degreeUri = null) }

            //  Same pattern as document pick
            CandidateHomeEvent.RequestPickDegree ->
                setEffect(CandidateHomeEffect.PickDegree)

            is CandidateHomeEvent.DegreeSelected ->
                setState { copy(degreeUri = event.uri) }

            // ── Submit ────────────────────────────────────────────────────────
            CandidateHomeEvent.ContinueToPayment -> validateAndContinue()
        }
    }

    // ── Validation ────────────────────────────────────────────────────────────

    private fun validateAndContinue() {
        val s      = state.value
        val errors = mutableMapOf<String, String>()

        if (s.fatherName.isBlank())  errors["fatherName"] = "Required"
        if (s.cnicNumber.isBlank())  errors["cnic"]       = "Required"
        if (s.dateOfBirth.isBlank()) errors["dob"]        = "Required"
        if (s.city.isBlank())        errors["city"]       = "Please select a city"
        if (s.address.isBlank())     errors["address"]    = "Required"

        val missing = DocumentType.entries.filter { it !in s.uploadedDocs }
        if (missing.isNotEmpty()) errors["docs"] = "Upload all mandatory documents"

        if (errors.isNotEmpty()) {
            setState { copy(errors = errors) }
            setEffect(CandidateHomeEffect.ShowSnackbar("Please complete all required fields"))
            return
        }

        viewModelScope.launch {
            setState { copy(isSubmitting = true) }
            delay(1200)   // TODO: replace with real API call
            setState { copy(isSubmitting = false) }
            setEffect(CandidateHomeEffect.NavigateToPayment)
        }
    }
}