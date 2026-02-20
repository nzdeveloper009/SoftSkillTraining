package com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEffect

sealed interface OtpEffect : UiEffect {
    /** Move focus to a specific box index. */
    data class MoveFocus(val index: Int)         : OtpEffect
    data object NavigateToHome                   : OtpEffect
    data object NavigateBack                     : OtpEffect
    data class ShowSnackbar(val message: String) : OtpEffect
}