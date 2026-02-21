package com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiState
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.document_upload.helper.CandidateTab
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.document_upload.helper.DocumentType
import com.cmp.community.healers.softskilltraining.theme.AppLanguage

data class CandidateHomeState(
    // Top bar
    val activeTab: CandidateTab = CandidateTab.REGISTRATION,
    val language:  AppLanguage  = AppLanguage.ENGLISH,

    // Progress stepper
    val currentStep: Int        = 1,
    val totalSteps:  Int        = 3,

    // Section collapse/expand
    val personalExpanded:  Boolean = true,
    val documentsExpanded: Boolean = true,
    val educationExpanded: Boolean = true,

    // Personal information form fields
    val fatherName:       String  = "",
    val cnicNumber:       String  = "",
    val dateOfBirth:      String  = "",
    val contactNumber:    String  = "",   // locked — comes from login phone
    val city:             String  = "",
    val address:          String  = "",
    val cityDropdownOpen: Boolean = false,

    // Documents  — map of DocumentType → URI string
    val uploadedDocs: Map<DocumentType, String> = emptyMap(),

    // Education
    val hasSixteenYearsEducation: Boolean = false,
    val degreeUri: String? = null,

    // Validation errors  — key matches field name
    val errors: Map<String, String> = emptyMap(),

    // Loading
    val isSubmitting: Boolean = false,
) : UiState