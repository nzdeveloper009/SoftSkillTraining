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
            is SignUpEvent.FirstNameChanged -> setState {
                copy(firstName = event.value, firstNameError = null, generalError = null)
            }
            is SignUpEvent.LastNameChanged -> setState {
                copy(lastName = event.value, lastNameError = null, generalError = null)
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
            SignUpEvent.Submit                   -> submit()
            SignUpEvent.NavigateToSignIn         -> setEffect(SignUpEffect.NavigateToSignIn)
        }
    }

    private fun submit() {
        val s = state.value

        val firstNameError = if (s.firstName.isBlank()) "First name is required" else null
        val lastNameError  = if (s.lastName.isBlank())  "Last name is required"  else null
        val emailError     = if (s.email.isBlank() || !s.email.contains("@")) "Enter a valid email" else null
        val phoneError     = if (s.phone.isBlank()) "Phone number is required" else null
        val passwordError  = if (s.password.length < 8) "Password must be at least 8 characters" else null
        val confirmError   = if (s.confirmPassword != s.password) "Passwords do not match" else null

        if (listOf(firstNameError, lastNameError, emailError, phoneError, passwordError, confirmError).any { it != null }) {
            setState {
                copy(
                    firstNameError = firstNameError,
                    lastNameError  = lastNameError,
                    emailError     = emailError,
                    phoneError     = phoneError,
                    passwordError  = passwordError,
                    confirmError   = confirmError
                )
            }
            return
        }

        viewModelScope.launch {
            setState { copy(isLoading = true, generalError = null) }

            when (val result = authRepository.signUp(
                firstName   = s.firstName.trim(),
                lastName    = s.lastName.trim(),
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