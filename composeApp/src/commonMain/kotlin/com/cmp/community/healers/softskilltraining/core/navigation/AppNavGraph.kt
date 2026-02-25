package com.cmp.community.healers.softskilltraining.core.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeEvent
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeState
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.home.ui.CandidateHomeScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.home.ui.CandidateScheduledHomeScreen
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
                    subclass(Screen.Payment::class, Screen.Payment.serializer())
                    subclass(Screen.Scheduling::class, Screen.Scheduling.serializer())
                    subclass(Screen.CandidateScheduledHome::class, Screen.CandidateScheduledHome.serializer())
                }
            }
        },
        Screen.SignIn
    )

    // ── Shared CandidateHomeViewModel — lazily initialized with real phone ────
    //
    // WHY NOT create it at graph scope:
    //   viewModel { CandidateHomeViewModel() }  ← phone would always be ""
    //
    // WHY this pattern works:
    //   1. CandidateHome entry gets key.phone from the route (set by SignIn/OTP)
    //   2. It creates the VM with the real phone and stores it here
    //   3. Payment and Scheduling entries read `candidateHomeVm` — guaranteed
    //      non-null because you can only reach them after CandidateHome composed
    //   4. On logout we null it out so the next session starts fresh
    var candidateHomeVm: CandidateHomeViewModel? by remember { mutableStateOf(null) }


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
                animationSpec = tween(300)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { -it / 3 },
                animationSpec = tween(300)
            )
        },

        // ── Pop transition: current screen slides out to right ────────────────
        popTransitionSpec = {
            slideInHorizontally(
                initialOffsetX = { -it / 3 },
                animationSpec = tween(300)
            ) togetherWith slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(300)
            )
        },


        entryProvider = entryProvider {

            // ── Sign In ───────────────────────────────────────────────────────
            entry<Screen.SignIn> {
                val vm: SignInViewModel = viewModel { SignInViewModel() }
                SignInScreen(
                    vm = vm,
                    onNavigateToHome = { phone ->
                        backStack.clear()
                        backStack.add(Screen.CandidateHome(phone))
                    },
                    onNavigateToSignUp = { backStack.add(Screen.SignUp) }
                )
            }

            // ── Sign Up ───────────────────────────────────────────────────────
            entry<Screen.SignUp> {
                val vm: SignUpViewModel = viewModel { SignUpViewModel() }
                SignUpScreen(
                    vm = vm,
                    onNavigateToOtp = { phone -> backStack.add(Screen.OtpVerify(phone)) },
                    onNavigateToSignIn = { backStack.removeLastOrNull() }
                )
            }

            // ── OTP Verify ────────────────────────────────────────────────────
            // Phone travels in the route key — no need for SavedStateHandle or arguments.
            entry<Screen.OtpVerify> { key ->
                val vm: OtpViewModel = viewModel { OtpViewModel(phone = key.phone) }
                OtpScreen(
                    phone = key.phone,
                    vm = vm,
                    onNavigateToHome = {
                        // Clear the entire auth stack, push Home
                        backStack.clear()
                        backStack.add(Screen.CandidateHome(key.phone))
                    },
                    onNavigateBack = { backStack.removeLastOrNull() }
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

            // ── Candidate Home  (Step 1 Registration  +  Profile tab) ─────────
            //
            // HOW TABS WORK:
            //   PROFILE tab     → CandidateHomeScreen switches content inline to
            //                     ProfileScreen (no nav push — same back-stack entry)
            //   APPLICATION tab → ViewModel reads candidateHomeVm.applicationStep
            //                     and emits the correct NavigateTo* effect below
            entry<Screen.CandidateHome>(
                metadata = NavDisplay.transitionSpec {
                    fadeIn(tween(250)) togetherWith fadeOut(tween(250))
                }
            ) { key ->
                val vm = viewModel { CandidateHomeViewModel(loggedInPhone = key.phone) }

                // Store in shared slot — only written once per session.
                // `candidateHomeVm` stays non-null for the entire candidate flow.
                if (candidateHomeVm == null) candidateHomeVm = vm

                CandidateHomeScreen(
                    vm       = vm,
                    onLogout = {
                        candidateHomeVm = null           // clear for next login
                        backStack.clear()
                        backStack.add(Screen.SignIn)
                    },
                    onNavigateToPayment       = { backStack.add(Screen.Payment) },
                    onNavigateToRegistration  = { /* already on this screen — no-op */ },
                    onNavigateToScheduling    = { backStack.add(Screen.Scheduling) },
                    onNavigateToScheduledHome = {
                        backStack.add(Screen.CandidateScheduledHome(key.phone))
                    }
                )
            }

            // ── Payment  (Step 2) ─────────────────────────────────────────────
            // Application tab on PaymentScreen → ViewModel is shared so the effect
            // is collected by CandidateHomeScreen's LaunchedEffect, which is still
            // alive in the back-stack. Navigation 3 keeps all entries alive.
            entry<Screen.Payment> {
                val sharedVm = requireNotNull(candidateHomeVm)
                val paymentVm = viewModel { PaymentViewModel() }
                PaymentScreen(
                    vm = paymentVm,
                    candidateHomeVm = sharedVm,
                    onLogout = {
                        backStack.clear()
                        backStack.add(Screen.SignIn)
                    },
                    onBackToRegistration = { backStack.removeLastOrNull() },
                    onContinueToScheduling = {
                        // Advance step in shared VM → Profile card shows "Paid"
                        sharedVm.onEvent(CandidateHomeEvent.MarkPaymentComplete)
                        backStack.add(Screen.Scheduling)
                    }
                )
            }

            // ── Scheduling  (Step 3) ──────────────────────────────────────────
            // Congrats phase renders inside SchedulingScreen — no separate route needed.
            entry<Screen.Scheduling> {
                val sharedVm = requireNotNull(candidateHomeVm)
                val schedulingVm = viewModel { SchedulingViewModel() }
                SchedulingScreen(
                    vm = schedulingVm,
                    candidateHomeVm = sharedVm,
                    onLogout = {
                        backStack.clear()
                        backStack.add(Screen.SignIn)
                    },
                    onBackToPayment = { backStack.removeLastOrNull() },
                    // Scheduling confirmed → advance step + store training details
                    onRegistrationDone = { date, time, center, address, city ->
                        sharedVm.onEvent(
                            CandidateHomeEvent.MarkSchedulingComplete(
                                trainingDate = date,
                                trainingTime = time,
                                trainingCenter = center,
                                trainingAddress = address,
                                trainingCity = city
                            )
                        )
                        // Return to CandidateHome — Profile tab will now show
                        // the full training card
                        backStack.clear()
                        backStack.add(Screen.CandidateHome(sharedVm.state.value.profilePhone))
                    }
                )
            }

            // ── Candidate Scheduled Home  (All steps complete) ────────────────
            // Shown when applicationStep == COMPLETE.
            // Application tab → "Training Already Scheduled" card
            // Profile tab     → ProfileScreen rendered inline (no nav push)
            // "Go to Profile" button → switches activeTab to PROFILE inside the VM
            entry<Screen.CandidateScheduledHome>(
                metadata = NavDisplay.transitionSpec {
                    fadeIn(tween(250)) togetherWith fadeOut(tween(250))
                }
            ) { key ->
                val vm = viewModel { CandidateHomeViewModel(loggedInPhone = key.phone) }
                if (candidateHomeVm == null) candidateHomeVm = vm

                CandidateScheduledHomeScreen(
                    candidateHomeVm = vm,
                    onLogout        = {
                        candidateHomeVm = null
                        backStack.clear()
                        backStack.add(Screen.SignIn)
                    }
                )
            }
        }
    )
}