package com.cmp.community.healers.softskilltraining.di

import com.cmp.community.healers.softskilltraining.core.network.httpClient
import com.cmp.community.healers.softskilltraining.data.remote.api.AuthApi
import com.cmp.community.healers.softskilltraining.data.remote.api.CandidateApi
import org.koin.dsl.module

val networkModule = module {
    single { httpClient }
    single { AuthApi(client = get()) }
    single { CandidateApi(client = get()) }
}