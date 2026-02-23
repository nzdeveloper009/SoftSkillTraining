package com.cmp.community.healers.softskilltraining.core.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


/**
 * Every auth destination is a NavKey represented as a @Serializable class/object.
 * Using a sealed interface keeps them grouped and type-safe.
 */
@Serializable
sealed interface Screen: NavKey {

    /** Initial sign-in screen */
    @Serializable
    data object SignIn : Screen, NavKey


    /** Sign-up / registration screen */
    @Serializable
    data object SignUp: Screen, NavKey

    /**
     * OTP verification screen.
     * [phone] is passed as part of the key so the screen can display it.
     */
    @Serializable
    data class OtpVerify(val phone:String): Screen, NavKey
    @Serializable data object Home    :  Screen, NavKey
    @Serializable data object CandidateHome    :  Screen, NavKey
    @Serializable data object Payment    :  Screen, NavKey
}