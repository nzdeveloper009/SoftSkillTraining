package com.cmp.community.healers.softskilltraining.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.mvi.SignInViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.ui.SignInScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.mvi.OtpViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.ui.OtpScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.mvi.SignUpViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.ui.SignUpScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.ui.SchedulingScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.home.ui.CandidateHomeScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.home.ui.HomeScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.ui.PaymentScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic


@Composable
fun AppNavGraph() {
    // ── Back stack ────────────────────────────────────────────────────────────
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Screen.SignIn::class, Screen.SignIn.serializer())
                    subclass(Screen.SignUp::class, Screen.SignUp.serializer())
                    subclass(Screen.OtpVerify::class, Screen.OtpVerify.serializer())
                    subclass(Screen.Home::class, Screen.Home.serializer())
                    subclass(Screen.CandidateHome::class, Screen.CandidateHome.serializer())
                    subclass(Screen.Payment::class,       Screen.Payment.serializer())
                    subclass(Screen.Scheduling::class,     Screen.Scheduling.serializer())
                }
            }
        },
        Screen.SignIn
    )

    // ── Hoisted shared ViewModel ──────────────────────────────────────────────
    // Lives at AppNavGraph scope — survives push/pop between CandidateHome ↔ Payment.
    // Both screens receive the SAME vm instance, so:
    //   • TopBar (tab selection, language) looks identical on both screens
    //   • Changing language on Payment screen is reflected when user goes Back
    //   • No duplicate state, no prop-drilling of CandidateHomeState
    val candidateHomeVm: CandidateHomeViewModel = viewModel { CandidateHomeViewModel() }


    // ── NavDisplay ────────────────────────────────────────────────────────────
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),

        // ── Push transition: new screen slides in from right ──────────────────
        transitionSpec = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec  = tween(300)
            ) togetherWith slideOutHorizontally(
                targetOffsetX  = { -it / 3 },
                animationSpec  = tween(300)
            )
        },

        // ── Pop transition: current screen slides out to right ────────────────
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec  = tween(300)
            ) togetherWith slideOutHorizontally(
                targetOffsetX  = { it },
                animationSpec  = tween(300)
            )
        },


        entryProvider = entryProvider {

            // ── Sign In ───────────────────────────────────────────────────────
            entry<Screen.SignIn> {
                val vm: SignInViewModel = viewModel { SignInViewModel() }
                SignInScreen(
                    vm = vm,
                    onNavigateToHome    = { phone -> backStack.add(Screen.CandidateHome(phone)) },
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
                    phone            = key.phone,
                    vm               = vm,
                    onNavigateToHome = {
                        // Clear the entire auth stack, push Home
                        backStack.clear()
                        backStack.add(Screen.CandidateHome(key.phone))
                    },
                    onNavigateBack   = { backStack.removeLastOrNull() }
                )
            }

            // ── Home (WebView) ────────────────────────────────────────────────
            entry<Screen.Home>(
                metadata = NavDisplay.transitionSpec {
                    fadeIn(tween(250)) togetherWith fadeOut(tween(250))
                }
            ) {
                HomeScreen(
                    onNavigateBack = { /* Home is root — nowhere to go back to */ },
                    // Candidate Portal card tapped → go to native SignUp screen
                    onNavigateToSignIn = { backStack.add(Screen.SignIn) }
                )
            }

            // ── Candidate Home  (Step 1 · Registration) ───────────────────────
            entry<Screen.CandidateHome>(
                metadata = NavDisplay.transitionSpec {
                    fadeIn(tween(250)) togetherWith fadeOut(tween(250))
                }
            ) {
                CandidateHomeScreen(
                    vm                  = candidateHomeVm,         // ← shared VM
                    onLogout            = {
                        backStack.clear()
                        backStack.add(Screen.SignIn)
                    },
                    onNavigateToPayment = { backStack.add(Screen.Payment) }
                )
            }

            // ── Payment  (Step 2) ─────────────────────────────────────────────
            entry<Screen.Payment> {
                val paymentVm = viewModel { PaymentViewModel() }

                PaymentScreen(
                    vm                     = paymentVm,
                    candidateHomeVm        = candidateHomeVm,      // ← same shared VM
                    onLogout               = {
                        backStack.clear()
                        backStack.add(Screen.SignIn)
                    },
                    onBackToRegistration   = { backStack.removeLastOrNull() },
                    onContinueToScheduling = {
                         backStack.add(Screen.Scheduling)
                    }
                )
            }

            // ── Scheduling  (Step 3) ──────────────────────────────────────────
            // Congrats phase renders inside SchedulingScreen — no separate route needed.
            entry<Screen.Scheduling> {
                val schedulingVm = viewModel { SchedulingViewModel() }
                SchedulingScreen(
                    vm                  = schedulingVm,
                    candidateHomeVm     = candidateHomeVm,
                    onLogout            = {
                        backStack.clear()
                        backStack.add(Screen.SignIn)
                    },
                    onBackToPayment     = { backStack.removeLastOrNull() },
                    onRegistrationDone  = {
                        // Registration fully complete — go back to Home (or Profile)
                        // Clear the entire candidate flow so the user can't go back
                        backStack.clear()
                        backStack.add(Screen.CandidateHome(""))
                    }
                )
            }
        }
    )
}