package com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEvent

sealed interface SignUpEvent : UiEvent {
    data class FirstNameChanged(val value: String)      : SignUpEvent
    data class LastNameChanged(val value: String)       : SignUpEvent
    data class EmailChanged(val value: String)          : SignUpEvent
    data class PhoneChanged(val value: String)          : SignUpEvent
    data class PasswordChanged(val value: String)       : SignUpEvent
    data class ConfirmPasswordChanged(val value: String): SignUpEvent
    data object TogglePasswordVisibility                : SignUpEvent
    data object ToggleConfirmVisibility                 : SignUpEvent
    data object Submit                                  : SignUpEvent
    data object NavigateToSignIn                        : SignUpEvent
}