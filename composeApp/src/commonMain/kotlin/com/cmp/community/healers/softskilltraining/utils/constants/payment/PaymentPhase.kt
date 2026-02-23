package com.cmp.community.healers.softskilltraining.utils.constants.payment

enum class PaymentPhase {
    /** Initial: method selector + Generate QR button */
    SELECT_METHOD,
    /** QR visible + Confirm Payment button */
    QR_SHOWN,
    /** Payment confirmed: receipt info + Download Receipt + success banner */
    PAID
}