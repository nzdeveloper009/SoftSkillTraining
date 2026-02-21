package com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEffect
import com.cmp.community.healers.softskilltraining.presentation.feature.registration.document_upload.helper.DocumentType

sealed interface CandidateHomeEffect : UiEffect {
    data object NavigateToLogin                          : CandidateHomeEffect
    data object NavigateToPayment                        : CandidateHomeEffect

    /** Screen should open a platform file picker for this document type. */
    data class PickDocument(val type: DocumentType)      : CandidateHomeEffect

    /** Screen should open a platform file picker for the degree upload. */
    data object PickDegree                               : CandidateHomeEffect

    data class ShowSnackbar(val message: String)         : CandidateHomeEffect
}