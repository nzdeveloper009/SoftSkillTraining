package com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEffect

sealed interface SchedulingEffect : UiEffect {
    data object NavigateBackToPayment   : SchedulingEffect
    data object NavigateToProfile       : SchedulingEffect
    data class  ShowSnackbar(val msg: String) : SchedulingEffect
}