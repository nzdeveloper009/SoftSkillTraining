package com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.mvi

import androidx.lifecycle.viewModelScope
import com.cmp.community.healers.softskilltraining.core.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignUpViewModel : BaseViewModel<SignUpState, SignUpEvent, SignUpEffect>(SignUpState()) {

    override fun handleEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.FullNameChanged -> setState {
                copy(
                    fullName = event.value,
                    fullNameError = null,
                    generalError = null
                )
            }

            is SignUpEvent.PhoneChanged -> setState {
                copy(
                    phone = event.value,
                    phoneError = null,
                    generalError = null
                )
            }

            is SignUpEvent.PasswordChanged -> setState {
                copy(
                    password = event.value,
                    passwordError = null,
                    generalError = null
                )
            }

            is SignUpEvent.ConfirmPasswordChanged -> setState {
                copy(
                    confirmPassword = event.value,
                    confirmError = null,
                    generalError = null
                )
            }

            SignUpEvent.TogglePasswordVisibility -> setState { copy(passwordVisible = !passwordVisible) }
            SignUpEvent.ToggleConfirmVisibility -> setState { copy(confirmVisible = !confirmVisible) }
            SignUpEvent.Submit -> submit()
            SignUpEvent.NavigateToSignIn -> setEffect(SignUpEffect.NavigateToSignIn)
        }
    }

    private fun submit() {
        val s = state.value

        val fullNameError = if (s.fullName.isBlank()) "Full name is required" else null
        val phoneError = if (s.phone.isBlank()) "Phone number is required" else null
        val passwordError = if (s.password.length < 8) "Password must be at least 8 chars" else null
        val confirmError = if (s.confirmPassword != s.password) "Passwords do not match" else null

        if (listOf(fullNameError, phoneError, passwordError, confirmError).any { it != null }) {
            setState {
                copy(
                    fullNameError = fullNameError,
                    phoneError = phoneError,
                    passwordError = passwordError,
                    confirmError = confirmError
                )
            }
            return
        }

        viewModelScope.launch {
            setState { copy(isLoading = true, generalError = null) }
            delay(1500) // TODO: replace with real repository call

            val success = true
            if (success) {
                setState { copy(isLoading = false) }
                setEffect(SignUpEffect.NavigateToOtp(phone = s.phone))
            } else {
                setState {
                    copy(
                        isLoading = false,
                        generalError = "Registration failed. Please try again."
                    )
                }
            }
        }
    }
}