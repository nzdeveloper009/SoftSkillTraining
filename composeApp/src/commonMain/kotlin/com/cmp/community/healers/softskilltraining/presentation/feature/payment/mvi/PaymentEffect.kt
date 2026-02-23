package com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiEffect

sealed interface PaymentEffect : UiEffect {
    data object NavigateBackToRegistration              : PaymentEffect
    data object NavigateToScheduling                    : PaymentEffect
    /**
     * Tells the Screen to trigger the platform-specific receipt download.
     * [receiptText] is pre-formatted plain-text content to save/share.
     */
    data class TriggerReceiptDownload(
        val transactionId: String,
        val paymentDate:   String,
        val amount:        String = "PKR 3,000",
        val receiptText:   String
    )                                                   : PaymentEffect
    data class ShowSnackbar(val message: String)        : PaymentEffect
}