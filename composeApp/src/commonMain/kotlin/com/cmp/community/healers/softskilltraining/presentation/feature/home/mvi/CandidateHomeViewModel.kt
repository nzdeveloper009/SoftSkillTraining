package com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi

import androidx.lifecycle.viewModelScope
import com.cmp.community.healers.softskilltraining.core.base.BaseViewModel
import com.cmp.community.healers.softskilltraining.core.datastore.AppPreferences
import com.cmp.community.healers.softskilltraining.core.network.NetworkResult
import com.cmp.community.healers.softskilltraining.core.storage.TokenStorage
import com.cmp.community.healers.softskilltraining.data.remote.dto.response.CandidateProfileData
import com.cmp.community.healers.softskilltraining.domain.model.City
import com.cmp.community.healers.softskilltraining.domain.repository.AuthRepository
import com.cmp.community.healers.softskilltraining.domain.repository.CandidateRepository
import com.cmp.community.healers.softskilltraining.utils.constants.homee.CandidateTab
import com.cmp.community.healers.softskilltraining.utils.constants.document.DocumentType
import com.cmp.community.healers.softskilltraining.utils.constants.document.documentTypeFromApiType
import com.cmp.community.healers.softskilltraining.theme.AppLanguage
import com.cmp.community.healers.softskilltraining.utils.constants.MONTH_NAMES
import com.cmp.community.healers.softskilltraining.utils.constants.application.ApplicationStep
import com.cmp.community.healers.softskilltraining.utils.constants.payment.FeePaymentStatus
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock

class CandidateHomeViewModel(
    loggedInPhone: String = "",
    private val authRepository: AuthRepository,
    private val candidateRepository: CandidateRepository,
    private val appPreferences: AppPreferences
) : BaseViewModel<CandidateHomeState, CandidateHomeEvent, CandidateHomeEffect>(
    CandidateHomeState(
        contactNumber = loggedInPhone,
        profilePhone  = loggedInPhone
    )
) {

    init {
        loadCities()
        loadProfile()
    }

    override fun handleEvent(event: CandidateHomeEvent) {
        when (event) {

            // ── Top bar ───────────────────────────────────────────────────────
            is CandidateHomeEvent.TabChanged -> handleTabChanged(event.tab)

            CandidateHomeEvent.ToggleLanguage ->
                setState { copy(language = if (language == AppLanguage.ENGLISH) AppLanguage.URDU else AppLanguage.ENGLISH) }

            CandidateHomeEvent.Logout -> logout()

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

            is CandidateHomeEvent.CityChanged -> {
                val cityId = state.value.cities.firstOrNull { it.name == event.value }?.id ?: ""
                setState { copy(city = event.value, cityId = cityId, errors = errors - "city") }
            }

            is CandidateHomeEvent.AddressChanged ->
                setState { copy(address = event.value, errors = errors - "address") }

            // ── Documents ─────────────────────────────────────────────────────
            is CandidateHomeEvent.RequestPickDocument ->
                setEffect(CandidateHomeEffect.PickDocument(event.type))

            is CandidateHomeEvent.DocumentSelected -> {
                setState { copy(uploadedDocs = uploadedDocs + (event.type to event.uri), errors = errors - "docs") }
                event.bytes?.let { bytes ->
                    uploadDocument(event.type, bytes, event.fileName)
                }
            }

            // ── Education ─────────────────────────────────────────────────────
            CandidateHomeEvent.ToggleSixteenYearsEducation ->
                setState { copy(hasSixteenYearsEducation = !hasSixteenYearsEducation, degreeUri = null) }

            CandidateHomeEvent.RequestPickDegree ->
                setEffect(CandidateHomeEffect.PickDegree)

            is CandidateHomeEvent.DegreeSelected -> {
                setState { copy(degreeUri = event.uri) }
                event.bytes?.let { bytes ->
                    uploadDegree(bytes, event.fileName)
                }
            }

            // ── Submit / navigation ───────────────────────────────────────────
            CandidateHomeEvent.ContinueToPayment       -> validateAndContinue()
            CandidateHomeEvent.NavigateToApplicationTab -> resumeApplication()

            // ── Step advancement (called from nav graph) ──────────────────────
            CandidateHomeEvent.MarkRegistrationComplete -> markRegistrationComplete()
            CandidateHomeEvent.MarkPaymentComplete      -> markPaymentComplete()
            is CandidateHomeEvent.MarkSchedulingComplete -> markSchedulingComplete(event)
        }
    }

    // ── Load cities from API ──────────────────────────────────────────────────

    private fun loadCities() {
        val token = TokenStorage.accessToken ?: return
        viewModelScope.launch {
            when (val result = candidateRepository.getCities(token)) {
                is NetworkResult.Success -> {
                    setState { copy(cities = result.data) }
                    // If profile already loaded a cityId, resolve its display name now
                    val cityId = state.value.cityId
                    if (cityId.isNotBlank()) {
                        val name = result.data.firstOrNull { it.id == cityId }?.name ?: ""
                        if (name.isNotBlank()) setState { copy(city = name) }
                    }
                }
                is NetworkResult.Error -> {
                    setEffect(CandidateHomeEffect.ShowSnackbar("Failed to load cities: ${result.message}"))
                }
            }
        }
    }

    // ── Load profile from API ─────────────────────────────────────────────────

    private fun loadProfile() {
        val token = TokenStorage.accessToken ?: return
        viewModelScope.launch {
            setState { copy(isLoadingProfile = true) }
            when (val result = candidateRepository.getProfile(token)) {
                is NetworkResult.Success -> populateStateFromProfile(result.data)
                is NetworkResult.Error   -> setEffect(CandidateHomeEffect.ShowSnackbar(result.message))
            }
            setState { copy(isLoadingProfile = false) }
        }
    }

    private fun populateStateFromProfile(data: CandidateProfileData) {
        val user     = data.user
        val fullName = if (user != null) "${user.firstName} ${user.lastName}".trim() else ""
        val email    = user?.email ?: ""
        val phone    = user?.phoneNumber ?: state.value.profilePhone

        // Map documents that already have a fileUrl into uploadedDocs
        val existingDocs = data.documents
            .filter { !it.fileUrl.isNullOrBlank() }
            .mapNotNull { doc -> documentTypeFromApiType(doc.type)?.let { it to (doc.fileUrl ?: "") } }
            .toMap()

        val isPaid    = data.payment?.isPaid == true
        val apiCityId = data.cityId ?: ""
        // Resolve display name if cities already loaded; loadCities() will also do this if it runs after
        val resolvedCityName = state.value.cities.firstOrNull { it.id == apiCityId }?.name ?: ""

        // ── Phase detection from profile data ─────────────────────────────────
        // examDate present → scheduling complete
        // isPaid → payment done, scheduling still needed
        // fatherName/cnic filled → registration done, payment still needed
        val detectedStep = when {
            data.examDate != null  -> ApplicationStep.COMPLETE
            isPaid                 -> ApplicationStep.SCHEDULING
            data.fatherName != null || data.cnic != null -> ApplicationStep.PAYMENT
            else                   -> ApplicationStep.REGISTRATION
        }

        // ── Days left to schedule (7-day window from payment date) ─────────────
        val daysLeft = data.payment?.paidAt?.let { calcDaysLeft(it) } ?: -1

        setState {
            copy(
                // Form fields from API
                fatherName               = data.fatherName ?: fatherName,
                cnicNumber               = data.cnic ?: cnicNumber,
                dateOfBirth              = data.dob?.let { isoToDisplay(it) } ?: dateOfBirth,
                cityId                   = apiCityId,
                city                     = resolvedCityName.ifBlank { city },
                address                  = data.address ?: address,
                hasSixteenYearsEducation = data.has16YearsEducation,
                contactNumber            = phone,

                // Profile display fields
                profileName              = fullName.ifBlank { profileName },
                profileCandidateId       = data.userId,
                profileEmail             = email.ifBlank { profileEmail },
                profilePhone             = phone,

                // Docs
                uploadedDocs             = existingDocs + uploadedDocs,

                // Payment / phase
                profileFeePaymentStatus  = if (isPaid) FeePaymentStatus.PAID else FeePaymentStatus.UNPAID,
                applicationStep          = detectedStep,
                currentStep              = when (detectedStep) {
                    ApplicationStep.REGISTRATION -> 1
                    ApplicationStep.PAYMENT      -> 2
                    ApplicationStep.SCHEDULING   -> 3
                    ApplicationStep.COMPLETE     -> 3
                },
                daysLeftToSchedule       = daysLeft
            )
        }

        // ── Auto-navigate to the current phase (once per login) ────────────────
        if (!state.value.hasAutoNavigated && detectedStep != ApplicationStep.REGISTRATION) {
            setState { copy(hasAutoNavigated = true) }
            when (detectedStep) {
                ApplicationStep.PAYMENT    -> setEffect(CandidateHomeEffect.NavigateToPayment)
                ApplicationStep.SCHEDULING -> setEffect(CandidateHomeEffect.NavigateToScheduling)
                ApplicationStep.COMPLETE   -> setEffect(CandidateHomeEffect.NavigateToCandidateScheduledHome)
                ApplicationStep.REGISTRATION -> Unit
            }
        }
    }

    private fun calcDaysLeft(paidAt: String): Int = try {
        val paidDate = Instant.parse(paidAt).toLocalDateTime(TimeZone.UTC).date
        val deadline = paidDate.plus(7, DateTimeUnit.DAY)
        val today    = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        (deadline.toEpochDays() - today.toEpochDays()).toInt().coerceAtLeast(0)
    } catch (e: Exception) { -1 }

    // ── Upload document to API ────────────────────────────────────────────────

    private fun uploadDocument(type: DocumentType, bytes: ByteArray, fileName: String) {
        val token = TokenStorage.accessToken ?: run {
            setEffect(CandidateHomeEffect.ShowSnackbar("Session expired. Please log in again."))
            return
        }
        viewModelScope.launch {
            setState { copy(uploadingDocTypes = uploadingDocTypes + type) }
            when (val result = candidateRepository.uploadDocument(token, type.apiType, bytes, fileName)) {
                is NetworkResult.Success -> { /* URI is already stored in state for display */ }
                is NetworkResult.Error   -> {
                    setEffect(CandidateHomeEffect.ShowSnackbar("Upload failed: ${result.message}"))
                    // Remove from uploaded docs so user can retry
                    setState { copy(uploadedDocs = uploadedDocs - type) }
                }
            }
            setState { copy(uploadingDocTypes = uploadingDocTypes - type) }
        }
    }

    private fun uploadDegree(bytes: ByteArray, fileName: String) {
        val token = TokenStorage.accessToken ?: run {
            setEffect(CandidateHomeEffect.ShowSnackbar("Session expired. Please log in again."))
            return
        }
        viewModelScope.launch {
            setState { copy(isDegreeUploading = true) }
            when (val result = candidateRepository.uploadDocument(token, "degreeTranscript", bytes, fileName)) {
                is NetworkResult.Success -> { /* degreeUri already in state */ }
                is NetworkResult.Error   -> {
                    setEffect(CandidateHomeEffect.ShowSnackbar("Degree upload failed: ${result.message}"))
                    setState { copy(degreeUri = null) }
                }
            }
            setState { copy(isDegreeUploading = false) }
        }
    }

    // ── Tab changed ───────────────────────────────────────────────────────────

    private fun handleTabChanged(tab: CandidateTab) {
        val wasOnProfile = state.value.activeTab == CandidateTab.PROFILE
        setState { copy(activeTab = tab) }

        if (tab == CandidateTab.REGISTRATION && wasOnProfile) {
            resumeApplication()
        }
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
        val today = Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault()).date
        val dateStr = "${monthName(today.monthNumber)} ${today.dayOfMonth}, ${today.year}"

        setState {
            copy(
                applicationStep         = ApplicationStep.PAYMENT,
                currentStep             = 2,
                profileFeePaymentStatus = FeePaymentStatus.UNPAID,
                profileRegistrationDate = dateStr,
                profileName             = fatherName.ifBlank { "N/A" },
                profileLocation          = "${city.ifBlank { "N/A" }}, Pakistan",
            )
        }
    }

    // ── Step: Payment complete ────────────────────────────────────────────────

    private fun markPaymentComplete() {
        setState {
            copy(
                applicationStep         = ApplicationStep.SCHEDULING,
                currentStep             = 3,
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

    // ── Validate form → call updateProfile API → navigate to Payment ──────────

    private fun validateAndContinue() {
        val s      = state.value
        val errors = mutableMapOf<String, String>()

        if (s.fatherName.isBlank())  errors["fatherName"] = "Required"
        if (s.cnicNumber.isBlank())  errors["cnic"]       = "Required"
        if (s.dateOfBirth.isBlank()) errors["dob"]        = "Required"
        if (s.address.isBlank())     errors["address"]    = "Required"
        // city is optional — UUID comes from the backend cities API (not yet implemented)

        val missing = DocumentType.entries.filter { it !in s.uploadedDocs }
        if (missing.isNotEmpty()) errors["docs"] = "Upload all mandatory documents"

        if (errors.isNotEmpty()) {
            setState { copy(errors = errors) }
            setEffect(CandidateHomeEffect.ShowSnackbar("Please complete all required fields"))
            return
        }

        val token = TokenStorage.accessToken
        if (token.isNullOrBlank()) {
            setEffect(CandidateHomeEffect.ShowSnackbar("Session expired. Please log in again."))
            return
        }

        viewModelScope.launch {
            setState { copy(isSubmitting = true) }
            val result = candidateRepository.updateProfile(
                accessToken         = token,
                cnic                = s.cnicNumber,
                fatherName          = s.fatherName,
                dob                 = displayToIso(s.dateOfBirth),
                address             = s.address,
                city                = s.cityId.takeIf { it.isNotBlank() },
                has16YearsEducation = s.hasSixteenYearsEducation
            )
            setState { copy(isSubmitting = false) }
            when (result) {
                is NetworkResult.Success -> {
                    onEvent(CandidateHomeEvent.MarkRegistrationComplete)
                    setEffect(CandidateHomeEffect.NavigateToPayment)
                }
                is NetworkResult.Error -> {
                    setEffect(CandidateHomeEffect.ShowSnackbar(result.message))
                }
            }
        }
    }

    // ── Logout — call API, clear tokens, navigate ─────────────────────────────

    private fun logout() {
        viewModelScope.launch {
            val token = TokenStorage.accessToken
            if (!token.isNullOrBlank()) {
                authRepository.logout(token)
            }
            TokenStorage.clear()
            appPreferences.clearAuthSession()
            setEffect(CandidateHomeEffect.NavigateToLogin)
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun monthName(m: Int) = MONTH_NAMES[m - 1]

    /** Convert ISO "YYYY-MM-DD" from API to display "DD/MM/YYYY" */
    private fun isoToDisplay(iso: String): String = try {
        val parts = iso.split("T").first().split("-")
        "${parts[2]}/${parts[1]}/${parts[0]}"
    } catch (e: Exception) { iso }

    /** Convert display "DD/MM/YYYY" from form to ISO "YYYY-MM-DD" for API */
    private fun displayToIso(display: String): String = try {
        val parts = display.split("/")
        "${parts[2]}-${parts[1]}-${parts[0]}"
    } catch (e: Exception) { display }

}