package com.cmp.community.healers.softskilltraining.presentation.feature.home.helper

/**
 * All portal cards on the home page and which native screen they open.
 * "/candidate/auth" → SignIn  (the card links to the auth page, not sign-up)
 */
sealed interface NativeDestination {
    data object SignIn         : NativeDestination   // Candidate Portal
    data object TrainingSignIn : NativeDestination   // Training Portal  (web-only for now)
    data object WebOnly        : NativeDestination   // open in WebView
}


val PATH_TO_NATIVE: Map<String, NativeDestination> = mapOf(
    "/candidate/auth" to NativeDestination.SignIn,
    "/training/auth"  to NativeDestination.WebOnly,   // swap to native when ready
    "/center/auth"    to NativeDestination.WebOnly,
    "/admin/auth"     to NativeDestination.WebOnly,
    "/ministry/auth"  to NativeDestination.WebOnly
)