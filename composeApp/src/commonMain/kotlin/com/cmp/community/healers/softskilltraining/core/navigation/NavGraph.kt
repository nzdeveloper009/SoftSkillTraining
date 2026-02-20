package com.cmp.community.healers.softskilltraining.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.ui.SignInScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.ui.OtpScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.ui.SignUpScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


@Composable
fun AuthNavGraph(
    onAuthSuccess: () -> Unit
) {
    // ── Back stack ────────────────────────────────────────────────────────────
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Screen.SignIn::class, Screen.SignIn.serializer())
                    subclass(Screen.SignUp::class, Screen.SignUp.serializer())
                    subclass(Screen.OtpVerify::class, Screen.OtpVerify.serializer())
                }
            }
        },
        Screen.SignIn
    )

    // ── NavDisplay ────────────────────────────────────────────────────────────
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        // ── Slide transitions ─────────────────────────────────────────────────
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(300)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(300)
            )
        },
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            )
        },

        // ── Entry provider ─────────────────────────────────────────────────────
        entryProvider = entryProvider {

            // Sign In
            entry<Screen.SignIn> {
                SignInScreen(
                    onSignIn = { phone, password ->
                        // TODO: call ViewModel.signIn(phone, password)
                        backStack.add(Screen.OtpVerify(phone = phone))
                    },
                    onSignUp = {
                        backStack.add(Screen.SignUp)
                    }
                )
            }

            // Sign Up
            entry<Screen.SignUp> {
                SignUpScreen(
                    onSignUp = { _, phone, _ ->
                        // TODO: call ViewModel.signUp(...)
                        backStack.add(Screen.OtpVerify(phone = phone))
                    },
                    onSignIn = {
                        backStack.removeLastOrNull()
                    }
                )
            }

            // OTP Verify — phone number travels inside the route key itself
            entry<Screen.OtpVerify> { key ->
                OtpScreen(
                    phoneNumber = key.phone,
                    onOtpVerified = { _ ->
                        // TODO: call ViewModel.verifyOtp(otp)
                        onAuthSuccess()
                    },
                    onBack = {
                        backStack.removeLastOrNull()
                    },
                    onResendOtp = {
                        // TODO: call ViewModel.resendOtp(key.phone)
                    }
                )
            }
        }
    )
}