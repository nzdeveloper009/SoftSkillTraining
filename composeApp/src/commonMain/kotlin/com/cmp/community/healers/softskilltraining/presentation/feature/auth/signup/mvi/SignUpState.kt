package com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiState

// ── State ──────────────────────────────────────────────────────────────────────
data class SignUpState(
    val fullName: String          = "",
    val phone: String             = "",
    val password: String          = "",
    val confirmPassword: String   = "",
    val passwordVisible: Boolean  = false,
    val confirmVisible: Boolean   = false,
    val isLoading: Boolean        = false,
    val fullNameError: String?    = null,
    val phoneError: String?       = null,
    val passwordError: String?    = null,
    val confirmError: String?     = null,
    val generalError: String?     = null
) : UiState