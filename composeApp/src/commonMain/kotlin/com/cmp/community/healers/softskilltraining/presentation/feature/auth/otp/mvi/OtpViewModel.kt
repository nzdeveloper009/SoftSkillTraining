package com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.mvi

import androidx.lifecycle.viewModelScope
import com.cmp.community.healers.softskilltraining.core.base.BaseViewModel
import com.cmp.community.healers.softskilltraining.utils.constants.OTP_LENGTH
import com.cmp.community.healers.softskilltraining.utils.constants.RESEND_COUNTDOWN_SECONDS
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class OtpViewModel(val phone: String) : BaseViewModel<OtpState, OtpEvent, OtpEffect>(OtpState()) {

    private var timerJob: Job? = null

    init {
        startCountdown()
    }

    override fun handleEvent(event: OtpEvent) {
        when (event) {

            is OtpEvent.DigitEntered -> {
                val digit = event.value.filter { it.isDigit() }.take(1)
                val newDigits = state.value.digits.toMutableList().also { it[event.index] = digit }
                setState { copy(digits = newDigits, isError = false, errorMessage = "") }

                if (digit.isNotEmpty()) {
                    val next = event.index + 1
                    if (next < OTP_LENGTH) {
                        setEffect(OtpEffect.MoveFocus(next))
                    } else {
                        // Last box filled — auto submit
                        submit(newDigits.joinToString(""))
                    }
                }
            }

            is OtpEvent.BackspacePressed -> {
                if (state.value.digits[event.index].isNotEmpty()) {
                    val newDigits = state.value.digits.toMutableList().also { it[event.index] = "" }
                    setState { copy(digits = newDigits, isError = false) }
                } else if (event.index > 0) {
                    val newDigits = state.value.digits.toMutableList().also { it[event.index - 1] = "" }
                    setState { copy(digits = newDigits, isError = false) }
                    setEffect(OtpEffect.MoveFocus(event.index - 1))
                }
            }

            OtpEvent.Submit -> {
                val otp = state.value.otp
                when {
                    otp.length < OTP_LENGTH -> setState { copy(isError = true, errorMessage = "Please enter all 6 digits") }
                    else -> submit(otp)
                }
            }

            OtpEvent.ResendOtp -> {
                // Reset digits + restart countdown
                setState {
                    copy(
                        digits = List(OTP_LENGTH) { "" },
                        isError = false,
                        errorMessage = "",
                        isExpired = false
                    )
                }
                setEffect(OtpEffect.MoveFocus(0))
                startCountdown()
            }

            OtpEvent.TimerTick -> {
                val current = state.value.resendTimer
                if (current > 1) {
                    setState { copy(resendTimer = current - 1) }
                } else {
                    // Timer expired — stop, show Resend
                    timerJob?.cancel()
                    setState { copy(resendTimer = 0, canResend = true, isExpired = true) }
                }
            }

            OtpEvent.NavigateBack -> setEffect(OtpEffect.NavigateBack)
        }
    }

    // ── Verify logic ──────────────────────────────────────────────────────────
    // Dummy rule: "111111" is the correct OTP
    private fun submit(otp: String) {
        // Don't allow submission after timer expired
        if (state.value.isExpired) {
            setState { copy(isError = true, errorMessage = "OTP expired. Please resend a new code.") }
            return
        }

        viewModelScope.launch {
            setState { copy(isLoading = true, isError = false) }
            delay(800) // simulate network latency

            if (otp == CORRECT_OTP) {
                timerJob?.cancel()
                setState { copy(isLoading = false) }
                setEffect(OtpEffect.NavigateToHome)
            } else {
                setState {
                    copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = "Incorrect OTP. Please try again.",
                        digits = List(OTP_LENGTH) { "" }
                    )
                }
                setEffect(OtpEffect.MoveFocus(0))
            }
        }
    }

    private fun startCountdown() {
        timerJob?.cancel()
        setState { copy(resendTimer = RESEND_COUNTDOWN_SECONDS, canResend = false, isExpired = false) }
        timerJob = viewModelScope.launch {
            repeat(RESEND_COUNTDOWN_SECONDS) {
                delay(1000)
                onEvent(OtpEvent.TimerTick)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    companion object {
        const val CORRECT_OTP = "111111"
    }
}