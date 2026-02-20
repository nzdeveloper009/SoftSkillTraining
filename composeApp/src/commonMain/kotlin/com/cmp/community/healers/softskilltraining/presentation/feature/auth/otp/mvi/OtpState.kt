package com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiState
import com.cmp.community.healers.softskilltraining.utils.constants.OTP_LENGTH
import com.cmp.community.healers.softskilltraining.utils.constants.RESEND_COUNTDOWN_SECONDS

data class OtpState(
    val digits: List<String>     = List(OTP_LENGTH) { "" },
    val isLoading: Boolean       = false,
    val isError: Boolean         = false,
    val errorMessage: String     = "",
    val resendTimer: Int         = RESEND_COUNTDOWN_SECONDS,
    val canResend: Boolean       = false
) : UiState {
    val otp: String get() = digits.joinToString("")
    val isFilled: Boolean get() = digits.all { it.isNotEmpty() }
}
