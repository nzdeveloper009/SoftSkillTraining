package com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.mvi

import androidx.lifecycle.viewModelScope
import com.cmp.community.healers.softskilltraining.core.base.BaseViewModel
import com.cmp.community.healers.softskilltraining.core.network.NetworkResult
import com.cmp.community.healers.softskilltraining.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class SignUpViewModel(
    private val authRepository: AuthRepository
) : BaseViewModel<SignUpState, SignUpEvent, SignUpEffect>(SignUpState()) {

    override fun handleEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.FullNameChanged -> setState {
                copy(fullName = event.value, fullNameError = null, generalError = null)
            }
            is SignUpEvent.EmailChanged -> setState {
                copy(email = event.value, emailError = null, generalError = null)
            }
            is SignUpEvent.PhoneChanged -> setState {
                copy(phone = event.value, phoneError = null, generalError = null)
            }
            is SignUpEvent.PasswordChanged -> setState {
                copy(password = event.value, passwordError = null, generalError = null)
            }
            is SignUpEvent.ConfirmPasswordChanged -> setState {
                copy(confirmPassword = event.value, confirmError = null, generalError = null)
            }
            SignUpEvent.TogglePasswordVisibility -> setState { copy(passwordVisible = !passwordVisible) }
            SignUpEvent.ToggleConfirmVisibility  -> setState { copy(confirmVisible = !confirmVisible) }
            SignUpEvent.Submit        -> submit()
            SignUpEvent.NavigateToSignIn -> setEffect(SignUpEffect.NavigateToSignIn)
        }
    }

    private fun submit() {
        val s = state.value

        val fullNameError = if (s.fullName.isBlank()) "Full name is required" else null
        val emailError    = if (s.email.isBlank() || !s.email.contains("@")) "Enter a valid email" else null
        val phoneError    = if (s.phone.isBlank()) "Phone number is required" else null
        val passwordError = if (s.password.length < 8) "Password must be at least 8 characters" else null
        val confirmError  = if (s.confirmPassword != s.password) "Passwords do not match" else null

        if (listOf(fullNameError, emailError, phoneError, passwordError, confirmError).any { it != null }) {
            setState {
                copy(
                    fullNameError = fullNameError,
                    emailError    = emailError,
                    phoneError    = phoneError,
                    passwordError = passwordError,
                    confirmError  = confirmError
                )
            }
            return
        }

        val nameParts = s.fullName.trim().split(" ", limit = 2)
        val firstName = nameParts[0]
        val lastName  = if (nameParts.size > 1) nameParts[1] else ""

        viewModelScope.launch {
            setState { copy(isLoading = true, generalError = null) }

            when (val result = authRepository.signUp(
                firstName   = firstName,
                lastName    = lastName,
                email       = s.email,
                phoneNumber = s.phone,
                password    = s.password
            )) {
                is NetworkResult.Success -> {
                    setState { copy(isLoading = false) }
                    setEffect(SignUpEffect.NavigateToOtp(phone = s.phone))
                }
                is NetworkResult.Error -> {
                    setState { copy(isLoading = false, generalError = result.message) }
                }
            }
        }
    }
}