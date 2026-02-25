package com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi

import androidx.lifecycle.viewModelScope
import com.cmp.community.healers.softskilltraining.core.base.BaseViewModel
import com.cmp.community.healers.softskilltraining.utils.constants.homee.CandidateTab
import com.cmp.community.healers.softskilltraining.utils.constants.document.DocumentType
import com.cmp.community.healers.softskilltraining.theme.AppLanguage
import com.cmp.community.healers.softskilltraining.utils.constants.MONTH_NAMES
import com.cmp.community.healers.softskilltraining.utils.constants.application.ApplicationStep
import com.cmp.community.healers.softskilltraining.utils.constants.payment.FeePaymentStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class CandidateHomeViewModel(
    loggedInPhone: String = ""
) : BaseViewModel<CandidateHomeState, CandidateHomeEvent, CandidateHomeEffect>(
    CandidateHomeState(
        contactNumber = loggedInPhone,
        profilePhone  = loggedInPhone
    )
) {

    override fun handleEvent(event: CandidateHomeEvent) {
        when (event) {

            // ── Top bar ───────────────────────────────────────────────────────
            is CandidateHomeEvent.TabChanged -> handleTabChanged(event.tab)

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

            // ── Documents ─────────────────────────────────────────────────────
            is CandidateHomeEvent.RequestPickDocument ->
                setEffect(CandidateHomeEffect.PickDocument(event.type))

            is CandidateHomeEvent.DocumentSelected ->
                setState { copy(uploadedDocs = uploadedDocs + (event.type to event.uri), errors = errors - "docs") }

            // ── Education ─────────────────────────────────────────────────────
            CandidateHomeEvent.ToggleSixteenYearsEducation ->
                setState { copy(hasSixteenYearsEducation = !hasSixteenYearsEducation, degreeUri = null) }

            CandidateHomeEvent.RequestPickDegree ->
                setEffect(CandidateHomeEffect.PickDegree)

            is CandidateHomeEvent.DegreeSelected ->
                setState { copy(degreeUri = event.uri) }

            // ── Submit / navigation ───────────────────────────────────────────
            CandidateHomeEvent.ContinueToPayment       -> validateAndContinue()
            CandidateHomeEvent.NavigateToApplicationTab -> resumeApplication()

            // ── Step advancement (called from nav graph) ──────────────────────
            CandidateHomeEvent.MarkRegistrationComplete -> markRegistrationComplete()
            CandidateHomeEvent.MarkPaymentComplete      -> markPaymentComplete()
            is CandidateHomeEvent.MarkSchedulingComplete -> markSchedulingComplete(event)
        }
    }

    // ── Tab changed ───────────────────────────────────────────────────────────
    // PROFILE tab   → just update activeTab — ProfileScreen renders inline on
    //                 whichever screen the user is currently on (no nav push).
    // APPLICATION tab → only emit a navigation effect when the user was viewing
    //                 their profile and needs to "resume" the application flow.
    //                 If they're already on Payment/Scheduling/etc, tapping
    //                 Application simply switches the tab back — the screen
    //                 re-shows its own content via AnimatedContent(activeTab).

    private fun handleTabChanged(tab: CandidateTab) {
        val wasOnProfile = state.value.activeTab == CandidateTab.PROFILE
        setState { copy(activeTab = tab) }

        if (tab == CandidateTab.REGISTRATION && wasOnProfile) {
            // User explicitly switched from Profile → Application.
            // Navigate to wherever they left off in the flow.
            resumeApplication()
        }
        // In all other cases (switching to Profile, or tapping Application while
        // already on the application flow) we only update the tab state — the
        // AnimatedContent in each screen handles the visual switch locally.
    }

    // ── Resume application at current step ────────────────────────────────────

    private fun resumeApplication() {
        val effect = when (state.value.applicationStep) {
            ApplicationStep.REGISTRATION -> CandidateHomeEffect.NavigateToRegistration
            ApplicationStep.PAYMENT      -> CandidateHomeEffect.NavigateToPayment
            ApplicationStep.SCHEDULING   -> CandidateHomeEffect.NavigateToScheduling
            ApplicationStep.COMPLETE     -> CandidateHomeEffect.NavigateToCandidateScheduledHome
        }
        setEffect(effect)
    }

    // ── Step: Registration complete ───────────────────────────────────────────

    private fun markRegistrationComplete() {
        val s    = state.value
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        val dateStr = "${monthName(today.monthNumber)} ${today.dayOfMonth}, ${today.year}"

        setState {
            copy(
                applicationStep        = ApplicationStep.PAYMENT,
                currentStep            = 2,
                profileFeePaymentStatus = FeePaymentStatus.UNPAID,
                profileRegistrationDate = dateStr,
                // Snapshot form values → profile display values
                profileName            = fatherName.ifBlank { "N/A" },
                profileLocation        = "${city.ifBlank { "N/A" }}, Pakistan",
            )
        }
    }

    // ── Step: Payment complete ────────────────────────────────────────────────

    private fun markPaymentComplete() {
        setState {
            copy(
                applicationStep        = ApplicationStep.SCHEDULING,
                currentStep            = 3,
                profileFeePaymentStatus = FeePaymentStatus.PAID,
            )
        }
    }

    // ── Step: Scheduling complete ─────────────────────────────────────────────

    private fun markSchedulingComplete(event: CandidateHomeEvent.MarkSchedulingComplete) {
        val label = "Scheduled - ${event.trainingDate}"
        setState {
            copy(
                applicationStep           = ApplicationStep.COMPLETE,
                scheduledTrainingDate     = event.trainingDate,
                scheduledTrainingTime     = event.trainingTime,
                scheduledTrainingCenter   = event.trainingCenter,
                scheduledTrainingAddress  = event.trainingAddress,
                scheduledTrainingCity     = event.trainingCity,
                trainingStatusLabel       = label
            )
        }
    }

    // ── Validate form → navigate to Payment ──────────────────────────────────

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
            delay(1200)
            setState { copy(isSubmitting = false) }
            onEvent(CandidateHomeEvent.MarkRegistrationComplete)
            setEffect(CandidateHomeEffect.NavigateToPayment)
        }
    }

    private fun monthName(m: Int) = MONTH_NAMES[m - 1]
}