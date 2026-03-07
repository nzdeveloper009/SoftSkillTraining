package com.cmp.community.healers.softskilltraining.di

import com.cmp.community.healers.softskilltraining.presentation.feature.auth.login.mvi.SignInViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.otp.mvi.OtpViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.auth.signup.mvi.SignUpViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.exam_scheduling.mvi.SchedulingViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.home.mvi.CandidateHomeViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.payment.mvi.PaymentViewModel
import com.cmp.community.healers.softskilltraining.presentation.feature.splash.SplashViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SplashViewModel)
    viewModelOf(::SignUpViewModel)
    viewModelOf(::SignInViewModel)
    viewModelOf(::PaymentViewModel)
    viewModelOf(::SchedulingViewModel)

    // ViewModels that need a runtime phone parameter
    viewModel { params ->
        OtpViewModel(phone = params.get(), authRepository = get(), appPreferences = get())
    }
    viewModel { params ->
        CandidateHomeViewModel(loggedInPhone = params.get(), authRepository = get(), appPreferences = get())
    }
}