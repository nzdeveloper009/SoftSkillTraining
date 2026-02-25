package com.cmp.community.healers.softskilltraining.utils.constants.application

enum class ApplicationStep {
    /** Step 1 — form not yet submitted */
    REGISTRATION,
    /** Step 2 — registration done, payment pending */
    PAYMENT,
    /** Step 3 — payment done, scheduling pending */
    SCHEDULING,
    /** All 3 steps complete */
    COMPLETE,
//    /** Error state */
//    ERROR
}