package com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.mvi

import androidx.lifecycle.viewModelScope
import com.cmp.community.healers.softskilltraining.core.base.BaseViewModel
import com.cmp.community.healers.softskilltraining.core.datastore.AppPreferences
import com.cmp.community.healers.softskilltraining.core.network.NetworkResult
import com.cmp.community.healers.softskilltraining.core.storage.TokenStorage
import com.cmp.community.healers.softskilltraining.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authRepository: AuthRepository,
    private val appPreferences: AppPreferences
) : BaseViewModel<SignInState, SignInEvent, SignInEffect>(SignInState()) {

    override fun handleEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.PhoneChanged -> setState {
                copy(phone = event.value, phoneError = null, generalError = null)
            }
            is SignInEvent.PasswordChanged -> setState {
                copy(password = event.value, passwordError = null, generalError = null)
            }
            SignInEvent.TogglePasswordVisibility -> setState { copy(passwordVisible = !passwordVisible) }
            SignInEvent.Submit        -> submit()
            SignInEvent.NavigateToSignUp -> setEffect(SignInEffect.NavigateToSignUp)
        }
    }

    private fun submit() {
        val current = state.value

        val phoneError    = if (current.phone.isBlank()) "Phone number is required" else null
        val passwordError = if (current.password.isBlank()) "Password is required" else null

        if (phoneError != null || passwordError != null) {
            setState { copy(phoneError = phoneError, passwordError = passwordError) }
            return
        }

        viewModelScope.launch {
            setState { copy(isLoading = true, generalError = null) }

            when (val result = authRepository.login(
                phoneNumber = current.phone,
                password    = current.password
            )) {
                is NetworkResult.Success -> {
                    val tokens = result.data
                    TokenStorage.save(tokens.accessToken, tokens.refreshToken)
                    appPreferences.saveAuthSession(tokens.accessToken, tokens.refreshToken, current.phone)
                    setState { copy(isLoading = false) }
                    setEffect(SignInEffect.NavigateToHome(phone = current.phone))
                }
                is NetworkResult.Error -> {
                    setState { copy(isLoading = false, generalError = result.message) }
                }
            }
        }
    }
}