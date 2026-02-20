package com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEvent

sealed interface OtpEvent : UiEvent {
    /** Called when the user types a digit into box [index]. */
    data class DigitEntered(val index: Int, val value: String) : OtpEvent
    /** Called when backspace is pressed on box [index]. */
    data class BackspacePressed(val index: Int)                : OtpEvent
    /** User tapped the Verify button. */
    data object Submit                                         : OtpEvent
    /** User tapped Resend. */
    data object ResendOtp                                      : OtpEvent
    /** Internal — timer tick from ViewModel. */
    data object TimerTick                                      : OtpEvent
    /** Back button pressed. */
    data object NavigateBack                                   : OtpEvent
}