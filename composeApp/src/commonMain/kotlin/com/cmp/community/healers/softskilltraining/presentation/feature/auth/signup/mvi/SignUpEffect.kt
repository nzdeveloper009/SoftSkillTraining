package com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEffect

// ── Effects ────────────────────────────────────────────────────────────────────
sealed interface SignUpEffect : UiEffect {
    data class NavigateToOtp(val phone: String) : SignUpEffect
    data object NavigateToSignIn                : SignUpEffect
    data class ShowSnackbar(val message: String): SignUpEffect
}