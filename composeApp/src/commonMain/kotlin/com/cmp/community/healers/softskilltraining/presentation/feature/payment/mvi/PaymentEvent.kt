package com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEvent

sealed interface PaymentEvent : UiEvent {
    data object GenerateQr                             : PaymentEvent
    data object ConfirmPayment                         : PaymentEvent
    data object DownloadReceipt                        : PaymentEvent
    data object BackToRegistration                     : PaymentEvent
    data object ContinueToScheduling                   : PaymentEvent
}