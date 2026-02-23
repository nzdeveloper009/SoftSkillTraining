package com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEvent
import com.cmp.community.healers.softskilltraining.utils.constants.payment.PaymentMethod

sealed interface PaymentEvent : UiEvent {
    data class SelectMethod(val method: PaymentMethod) : PaymentEvent
    data object GenerateQr                             : PaymentEvent
    data object ConfirmPayment                         : PaymentEvent
    data object DownloadReceipt                        : PaymentEvent
    data object BackToRegistration                     : PaymentEvent
    data object ContinueToScheduling                   : PaymentEvent
}