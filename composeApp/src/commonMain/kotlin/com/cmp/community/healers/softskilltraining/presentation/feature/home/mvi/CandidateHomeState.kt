package com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiState
import com.cmp.community.healers.softskilltraining.utils.constants.homee.CandidateTab
import com.cmp.community.healers.softskilltraining.utils.constants.document.DocumentType
import com.cmp.community.healers.softskilltraining.theme.AppLanguage
import com.cmp.community.healers.softskilltraining.utils.constants.application.ApplicationStep
import com.cmp.community.healers.softskilltraining.utils.constants.payment.FeePaymentStatus

data class CandidateHomeState(

    // ── Top bar ───────────────────────────────────────────────────────────────
    val activeTab: CandidateTab = CandidateTab.REGISTRATION,
    val language:  AppLanguage  = AppLanguage.ENGLISH,

    // ── Application step (drives tab navigation resume logic) ─────────────────
    // Updated by ViewModel whenever a step is completed.
    // When user taps Application tab → nav graph pushes the correct screen.
    val applicationStep: ApplicationStep = ApplicationStep.REGISTRATION,

    // ── Progress stepper ──────────────────────────────────────────────────────
    val currentStep: Int        = 1,
    val totalSteps:  Int        = 3,

    // ── Section collapse/expand ───────────────────────────────────────────────
    val personalExpanded:  Boolean = true,
    val documentsExpanded: Boolean = true,
    val educationExpanded: Boolean = true,

    // ── Personal information form fields ──────────────────────────────────────
    val fatherName:       String  = Profile().fatherName,
    val cnicNumber:       String  = Profile().cnicNumber,
    val dateOfBirth:      String  = Profile().dateOfBirth,
    val contactNumber:    String  = Profile().contactNumber,   // locked — comes from login phone
    val city:             String  = Profile().city,
    val address:          String  = Profile().address,
    val cityDropdownOpen: Boolean = false,

    // ── Documents ─────────────────────────────────────────────────────────────
    val uploadedDocs: Map<DocumentType, String> = emptyMap(),

    // ── Education ─────────────────────────────────────────────────────────────
    val hasSixteenYearsEducation: Boolean = false,
    val degreeUri: String? = null,

    // ── Profile data (populated after registration completes) ─────────────────
    // These mirror the form fields above but are "confirmed" values shown on Profile.
    val profileName:           String = Profile().profileName,
    val profileCandidateId:    String = Profile().profileCandidateId,
    val profileEmail:          String = Profile().profileEmail,
    val profilePhone:          String = Profile().profilePhone,     // same as contactNumber
    val profileLocation:       String = Profile().profileLocation,
    val profileRegistrationDate: String = Profile().profileRegistrationDate,
    val profileFeePaymentStatus: FeePaymentStatus = Profile().profileFeePaymentStatus,

    // Training info (populated after scheduling completes)
    val scheduledTrainingDate:    String =Profile().scheduledTrainingDate,
    val scheduledTrainingTime:    String = Profile().scheduledTrainingTime,
    val scheduledTrainingCenter:  String = Profile().scheduledTrainingCenter,
    val scheduledTrainingAddress: String = Profile().scheduledTrainingAddress,
    val scheduledTrainingCity:    String = Profile().scheduledTrainingCity,

    // Application status badges
    val registrationStatus: String = Profile().registrationStatus,
    val trainingStatusLabel:String = Profile().trainingStatusLabel,         // e.g. "Scheduled - Feb 27, 2026"

    // ── Validation errors ─────────────────────────────────────────────────────
    val errors: Map<String, String> = emptyMap(),

    // ── Loading ───────────────────────────────────────────────────────────────
    val isSubmitting: Boolean = false,

    ) : UiState


data class ProfileSuccessFul(
    // ── Personal information form fields
    val fatherName:       String  = "Muhammad Mohsan",
    val cnicNumber:       String  = "34201-4555422-7",
    val dateOfBirth:      String  = "13/03/2000",
    val contactNumber:    String  = "03060619920",   // locked — comes from login phone
    val city:             String  = "Islamabad",
    val address:          String  = "Morgah Village",
    // ── Profile data (populated after registration completes)
    val profileName:           String = "Syed Nokhaiz",
    val profileCandidateId:    String = "468cd1bf-720d-4480-8c07-327adc4c7049",
    val profileEmail:          String = "msyednokhaiz@gmail.com",
    val profilePhone:          String = "03060619220",     // same as contactNumber
    val profileLocation:       String = "Islamabad, Pakistan",
    val profileRegistrationDate: String = "February 21, 2026",
    val profileFeePaymentStatus: FeePaymentStatus = FeePaymentStatus.PAID,

    // Training info (populated after scheduling completes)
    val scheduledTrainingDate:    String = "February 27, 2026",
    val scheduledTrainingTime:    String = "10:00 AM",
    val scheduledTrainingCenter:  String = "Islamaabd Test Center",
    val scheduledTrainingAddress: String = "123 Main Street, Karachi",
    val scheduledTrainingCity:    String = "Islamabad",

    // Application status badges
    val registrationStatus: String = "Completed",
    val trainingStatusLabel:String = "Scheduled - Feb 27, 2026",         // e.g. "Scheduled - Feb 27, 2026"
)
data class Profile(
    // ── Personal information form fields
    val fatherName:       String  = "",
    val cnicNumber:       String  = "",
    val dateOfBirth:      String  = "",
    val contactNumber:    String  = "",   // locked — comes from login phone
    val city:             String  = "",
    val address:          String  = "",
    // ── Profile data (populated after registration completes)
    val profileName:           String = "N/A",
    val profileCandidateId:    String = "N/A",
    val profileEmail:          String = "N/A",
    val profilePhone:          String = "",     // same as contactNumber
    val profileLocation:       String = "N/A, Pakistan",
    val profileRegistrationDate: String = "N/A",
    val profileFeePaymentStatus: FeePaymentStatus = FeePaymentStatus.UNPAID,

    // Training info (populated after scheduling completes)
    val scheduledTrainingDate:    String = "",
    val scheduledTrainingTime:    String = "10:00 AM",
    val scheduledTrainingCenter:  String = "",
    val scheduledTrainingAddress: String = "",
    val scheduledTrainingCity:    String = "",

    // Application status badges
    val registrationStatus: String = "Completed",
    val trainingStatusLabel:String = "",         // e.g. "Scheduled - Feb 27, 2026"
)