package com.cmp.community.healers.softskilltraining.presentation.feature.auth.nav

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.cmp.community.healers.softskilltraining.core.navigation.Screen
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.mvi.SignInViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.ui.SignInScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.mvi.OtpViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.ui.OtpScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.mvi.SignUpViewModel
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
        // Push: slide in from right, current slides left
        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it / 3 }, animationSpec = tween(300))
        },
        // Pop: reverse
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(300)) togetherWith
                    slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
        },


        entryProvider = entryProvider {

            // ── Sign In ───────────────────────────────────────────────────────
            entry<Screen.SignIn> {
                val vm: SignInViewModel = viewModel { SignInViewModel() }
                SignInScreen(
                    vm = vm,
                    onNavigateToOtp    = { phone -> backStack.add(Screen.OtpVerify(phone)) },
                    onNavigateToSignUp = { backStack.add(Screen.SignUp) }
                )
            }

            // ── Sign Up ───────────────────────────────────────────────────────
            entry<Screen.SignUp> {
                val vm: SignUpViewModel = viewModel { SignUpViewModel() }
                SignUpScreen(
                    vm = vm,
                    onNavigateToOtp     = { phone -> backStack.add(Screen.OtpVerify(phone)) },
                    onNavigateToSignIn  = { backStack.removeLastOrNull() }
                )
            }

            // ── OTP Verify ────────────────────────────────────────────────────
            // Phone travels in the route key — no need for SavedStateHandle or arguments.
            entry<Screen.OtpVerify> { key ->
                val vm: OtpViewModel = viewModel { OtpViewModel(phone = key.phone) }
                OtpScreen(
                    phone  = key.phone,
                    vm     = vm,
                    onNavigateToHome = { onAuthSuccess() },
                    onNavigateBack   = { backStack.removeLastOrNull() }
                )
            }
        }
    )
}