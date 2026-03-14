package com.cmp.community.healers.softskilltraining.presentation.feature.splash

import androidx.lifecycle.viewModelScope
import com.cmp.community.healers.softskilltraining.core.base.BaseViewModel
import com.cmp.community.healers.softskilltraining.core.base.UiEvent
import com.cmp.community.healers.softskilltraining.core.base.UiState
import com.cmp.community.healers.softskilltraining.core.datastore.AppPreferences
import com.cmp.community.healers.softskilltraining.core.network.NetworkResult
import com.cmp.community.healers.softskilltraining.core.storage.TokenStorage
import com.cmp.community.healers.softskilltraining.domain.repository.AuthRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object SplashState : UiState
object SplashEvent : UiEvent

class SplashViewModel(
    private val appPreferences: AppPreferences,
    private val authRepository: AuthRepository
) : BaseViewModel<SplashState, SplashEvent, SplashEffect>(SplashState) {

    init {
        checkAuthState()
    }

    override fun handleEvent(event: SplashEvent) = Unit

    private fun checkAuthState() {
        viewModelScope.launch {
            val storedRefreshToken = appPreferences.refreshToken.first()
            val phone              = appPreferences.loggedInPhone.first()

            if (!storedRefreshToken.isNullOrBlank() && !phone.isNullOrBlank()) {
                // Always attempt a token refresh so we have a valid access token
                when (val result = authRepository.refreshToken(storedRefreshToken)) {
                    is NetworkResult.Success -> {
                        val tokens = result.data
                        appPreferences.saveAuthSession(tokens.accessToken, tokens.refreshToken, phone)
                        TokenStorage.save(tokens.accessToken, tokens.refreshToken)
                        setEffect(SplashEffect.NavigateToCandidateHome(phone))
                    }
                    is NetworkResult.Error -> {
                        // Refresh token is also expired or invalid — force re-login
                        appPreferences.clearAuthSession()
                        TokenStorage.clear()
                        setEffect(SplashEffect.NavigateToSignIn)
                    }
                }
            } else {
                setEffect(SplashEffect.NavigateToSignIn)
            }
        }
    }
}