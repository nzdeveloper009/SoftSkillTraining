package com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiState


// ── State ──────────────────────────────────────────────────────────────────────
data class SignInState(
    val phone: String        = "",
    val password: String     = "",
    val isLoading: Boolean   = false,
    val phoneError: String?  = null,
    val passwordError: String? = null,
    val generalError: String? = null
) : UiState