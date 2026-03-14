package com.cmp.community.healers.softskilltraining.core.network

object ApiConstants {
    const val BASE_URL = "https://ministry-backend-2jdm.onrender.com"

    object Endpoints {
        const val SIGNUP_REQUEST       = "/auth/signup/request"
        const val SIGNUP_VERIFY        = "/auth/signup/verify"
        const val LOGIN_CANDIDATE      = "/auth/login/candidate"
        const val LOGOUT               = "/auth/logout"
        const val REFRESH_TOKEN        = "/auth/refresh-token"

        const val CANDIDATE_ME         = "/candidates/me"
        const val CANDIDATE_DOCUMENTS  = "/candidates/me/documents"
        const val CANDIDATE_SCHEDULE   = "/candidates/me/schedule"
        const val INITIATE_PAYMENT     = "/candidates/payments/initiate"
        const val CITIES               = "/super-admin/cities"
    }
}