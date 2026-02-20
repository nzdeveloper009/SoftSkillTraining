package com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEvent


// ── Events (user intents) ──────────────────────────────────────────────────────

sealed interface SignInEvent : UiEvent {
    data class PhoneChanged(val value: String)    : SignInEvent
    data class PasswordChanged(val value: String) : SignInEvent
    data object Submit                            : SignInEvent
    data object NavigateToSignUp                  : SignInEvent
}