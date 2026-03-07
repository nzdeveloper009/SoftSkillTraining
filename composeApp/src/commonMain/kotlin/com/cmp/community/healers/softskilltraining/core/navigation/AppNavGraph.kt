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
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.home.ui.CandidateHomeScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.home.ui.CandidateScheduledHomeScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.home.ui.HomeScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.ui.PaymentScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.splash.SplashScreen
import com.cmp.community.healers.softskilltraining.presentation.feature.splash.SplashViewModel
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf


@Composable
fun AppNavGraph() {
    // ── Back stack — starts at Splash (resolves auth state) ───────────────────
    val backStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Screen.Splash::class, Screen.Splash.serializer())
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
        Screen.Splash           // ← auth gate — no flicker to SignIn
    )

    // ── Shared CandidateHomeViewModel slot ───────────────────────────────────
    var candidateHomeVm: CandidateHomeViewModel? by remember { mutableStateOf(null) }

    // ── NavDisplay ────────────────────────────────────────────────────────────
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),

        transitionSpec = {
            slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it / 3 }, animationSpec = tween(300))
        },
        popTransitionSpec = {
            slideInHorizontally(initialOffsetX = { -it / 3 }, animationSpec = tween(300)) togetherWith
                    slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300))
        },

        entryProvider = entryProvider {

            // ── Splash ────────────────────────────────────────────────────────
            entry<Screen.Splash>(
                metadata = NavDisplay.transitionSpec {
                    fadeIn(tween(300)) togetherWith fadeOut(tween(300))
                }
            ) {
                val vm: SplashViewModel = koinViewModel()
                SplashScreen(
                    vm = vm,
                    onNavigateToSignIn = {
                        backStack.clear()
                        backStack.add(Screen.SignIn)
                    },
                    onNavigateToCandidateHome = { phone ->
                        backStack.clear()
                        backStack.add(Screen.CandidateHome(phone))
                    }
                )
            }

            // ── Sign In ───────────────────────────────────────────────────────
            entry<Screen.SignIn> {
                val vm: SignInViewModel = koinViewModel()
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
                val vm: SignUpViewModel = koinViewModel()
                SignUpScreen(
                    vm = vm,
                    onNavigateToOtp = { phone -> backStack.add(Screen.OtpVerify(phone)) },
                    onNavigateToSignIn = { backStack.removeLastOrNull() }
                )
            }

            // ── OTP Verify ────────────────────────────────────────────────────
            entry<Screen.OtpVerify> { key ->
                val vm: OtpViewModel = koinViewModel(parameters = { parametersOf(key.phone) })
                OtpScreen(
                    phone = key.phone,
                    vm = vm,
                    onNavigateToHome = {
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
                    onNavigateBack = {},
                    onNavigateToSignIn = { backStack.add(Screen.SignIn) }
                )
            }

            // ── Candidate Home ────────────────────────────────────────────────
            entry<Screen.CandidateHome>(
                metadata = NavDisplay.transitionSpec {
                    fadeIn(tween(250)) togetherWith fadeOut(tween(250))
                }
            ) { key ->
                val vm: CandidateHomeViewModel = koinViewModel(parameters = { parametersOf(key.phone) })
                if (candidateHomeVm == null) candidateHomeVm = vm

                CandidateHomeScreen(
                    vm = vm,
                    onLogout = {
                        candidateHomeVm = null
                        backStack.clear()
                        backStack.add(Screen.SignIn)
                    },
                    onNavigateToPayment      = { backStack.add(Screen.Payment) },
                    onNavigateToRegistration = {},
                    onNavigateToScheduling   = { backStack.add(Screen.Scheduling) },
                    onNavigateToScheduledHome = {
                        backStack.add(Screen.CandidateScheduledHome(key.phone))
                    }
                )
            }

            // ── Payment ───────────────────────────────────────────────────────
            entry<Screen.Payment> {
                val sharedVm = requireNotNull(candidateHomeVm)
                val paymentVm: PaymentViewModel = koinViewModel()
                PaymentScreen(
                    vm = paymentVm,
                    candidateHomeVm = sharedVm,
                    onLogout = {
                        backStack.clear()
                        backStack.add(Screen.SignIn)
                    },
                    onBackToRegistration = { backStack.removeLastOrNull() },
                    onContinueToScheduling = {
                        sharedVm.onEvent(CandidateHomeEvent.MarkPaymentComplete)
                        backStack.add(Screen.Scheduling)
                    }
                )
            }

            // ── Scheduling ────────────────────────────────────────────────────
            entry<Screen.Scheduling> {
                val sharedVm = requireNotNull(candidateHomeVm)
                val schedulingVm: SchedulingViewModel = koinViewModel()
                SchedulingScreen(
                    vm = schedulingVm,
                    candidateHomeVm = sharedVm,
                    onLogout = {
                        backStack.clear()
                        backStack.add(Screen.SignIn)
                    },
                    onBackToPayment = { backStack.removeLastOrNull() },
                    onRegistrationDone = { date, time, center, address, city ->
                        sharedVm.onEvent(
                            CandidateHomeEvent.MarkSchedulingComplete(
                                trainingDate    = date,
                                trainingTime    = time,
                                trainingCenter  = center,
                                trainingAddress = address,
                                trainingCity    = city
                            )
                        )
                        backStack.clear()
                        backStack.add(Screen.CandidateHome(sharedVm.state.value.profilePhone))
                    }
                )
            }

            // ── Candidate Scheduled Home ──────────────────────────────────────
            entry<Screen.CandidateScheduledHome>(
                metadata = NavDisplay.transitionSpec {
                    fadeIn(tween(250)) togetherWith fadeOut(tween(250))
                }
            ) { key ->
                val vm: CandidateHomeViewModel = koinViewModel(parameters = { parametersOf(key.phone) })
                if (candidateHomeVm == null) candidateHomeVm = vm

                CandidateScheduledHomeScreen(
                    candidateHomeVm = vm,
                    onLogout = {
                        candidateHomeVm = null
                        backStack.clear()
                        backStack.add(Screen.SignIn)
                    }
                )
            }
        }
    )
}