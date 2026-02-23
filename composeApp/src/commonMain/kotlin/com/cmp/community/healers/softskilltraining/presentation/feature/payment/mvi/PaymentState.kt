package com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi

import com.cmp.community.healers.softskilltraining.core.base.UiState
import com.cmp.community.healers.softskilltraining.utils.constants.payment.PaymentMethod
import com.cmp.community.healers.softskilltraining.utils.constants.payment.PaymentPhase

data class PaymentState(
    val phase:             PaymentPhase  = PaymentPhase.SELECT_METHOD,
    val selectedMethod: PaymentMethod = PaymentMethod.CREDIT_DEBIT,

    // QR phase
    val qrContent:         String        = "",   // URL/data encoded in QR
    val isGeneratingQr:    Boolean       = false,

    // Confirm phase
    val isConfirming:      Boolean       = false,

    // Paid phase
    val transactionId:     String        = "",
    val paymentDate:       String        = "",
    val isDownloading:     Boolean       = false,
) : UiState