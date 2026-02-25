package com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEffect


// ── Effects (one-shot side-effects) ───────────────────────────────────────────
sealed interface SignInEffect : UiEffect {
    data class NavigateToHome(val phone: String) : SignInEffect
    data object NavigateToSignUp                : SignInEffect
    data class ShowSnackbar(val message: String): SignInEffect
}