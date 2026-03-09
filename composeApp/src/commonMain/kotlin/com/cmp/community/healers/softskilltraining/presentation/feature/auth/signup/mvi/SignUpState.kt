package com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiState

data class SignUpState(
    val firstName: String         = "",
    val lastName: String          = "",
    val email: String             = "",
    val phone: String             = "",
    val password: String          = "",
    val confirmPassword: String   = "",
    val passwordVisible: Boolean  = false,
    val confirmVisible: Boolean   = false,
    val isLoading: Boolean        = false,
    val firstNameError: String?   = null,
    val lastNameError: String?    = null,
    val emailError: String?       = null,
    val phoneError: String?       = null,
    val passwordError: String?    = null,
    val confirmError: String?     = null,
    val generalError: String?     = null
) : UiState