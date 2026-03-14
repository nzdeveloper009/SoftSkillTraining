package com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiState
import com.cmp.community.healers.softskilltraining.utils.constants.payment.PaymentPhase

data class PaymentState(
    val phase:             PaymentPhase  = PaymentPhase.SELECT_METHOD,

    // QR phase — populated from initiatePayment API response
    val qrCodeBase64:      String        = "",
    val paymentId:         String        = "",
    val isGeneratingQr:    Boolean       = false,

    // Confirm phase
    val isConfirming:      Boolean       = false,

    // Paid phase
    val transactionId:     String        = "",
    val paymentDate:       String        = "",
    val isDownloading:     Boolean       = false,
) : UiState