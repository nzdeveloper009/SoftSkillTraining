package com.cmp.community.healers.softskilltraining.presentation.feature.splash

import androidx.lifecycle.viewModelScope
import com.cmp.community.healers.softskilltraining.core.base.BaseViewModel
import com.cmp.community.healers.softskilltraining.core.base.UiEvent
import com.cmp.community.healers.softskilltraining.core.base.UiState
import com.cmp.community.healers.softskilltraining.core.datastore.AppPreferences
import com.cmp.community.healers.softskilltraining.core.storage.TokenStorage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

object SplashState : UiState
object SplashEvent : UiEvent

class SplashViewModel(
    private val appPreferences: AppPreferences
) : BaseViewModel<SplashState, SplashEvent, SplashEffect>(SplashState) {

    init {
        checkAuthState()
    }

    override fun handleEvent(event: SplashEvent) = Unit

    private fun checkAuthState() {
        viewModelScope.launch {
            val accessToken = appPreferences.accessToken.first()
            val phone       = appPreferences.loggedInPhone.first()
            val refreshToken = appPreferences.refreshToken.first()

            if (!accessToken.isNullOrBlank() && !phone.isNullOrBlank()) {
                // Restore in-memory token so authenticated API calls work immediately
                TokenStorage.save(accessToken, refreshToken ?: "")
                setEffect(SplashEffect.NavigateToCandidateHome(phone))
            } else {
                setEffect(SplashEffect.NavigateToSignIn)
            }
        }
    }
}