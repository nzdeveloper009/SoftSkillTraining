package com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.mvi


import androidx.lifecycle.viewModelScope
import com.cmp.community.healers.softskilltraining.core.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SignInViewModel : BaseViewModel<SignInState, SignInEvent, SignInEffect>(SignInState()) {

    override fun handleEvent(event: SignInEvent) {
        when (event) {

            is SignInEvent.PhoneChanged -> setState {
                copy(phone = event.value, phoneError = null, generalError = null)
            }

            is SignInEvent.PasswordChanged -> setState {
                copy(password = event.value, passwordError = null, generalError = null)
            }

            SignInEvent.Submit -> submit()

            SignInEvent.NavigateToSignUp -> setEffect(SignInEffect.NavigateToSignUp)
        }
    }

    // ── Validation + API call ─────────────────────────────────────────────────
    private fun submit() {
        val current = state.value

        // Client-side validation
        val phoneError    = if (current.phone.isBlank()) "Phone number is required" else null
        val passwordError = if (current.password.isBlank()) "Password is required" else null

        if (phoneError != null || passwordError != null) {
            setState { copy(phoneError = phoneError, passwordError = passwordError) }
            return
        }

        // Simulate API call
        viewModelScope.launch {
            setState { copy(isLoading = true, generalError = null) }
            delay(1500) // Replace with real repo call

            val success = true
            if (success) {
                setState { copy(isLoading = false) }
                setEffect(SignInEffect.NavigateToHome(phone = current.phone))
            } else {
                setState {
                    copy(isLoading = false, generalError = "Invalid credentials. Please try again.")
                }
            }
        }
    }
}
