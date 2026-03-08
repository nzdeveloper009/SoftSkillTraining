package com.cmp.community.healers.softskilltraining.presentation.feature.splash

import com.cmp.community.healers.softskilltraining.core.base.UiEffect

sealed interface SplashEffect : UiEffect {
    data class NavigateToCandidateHome(val phone: String) : SplashEffect
    data object NavigateToSignIn : SplashEffect
}