package com.cmp.community.healers.softskilltraining.di

import com.cmp.community.healers.softskilltraining.core.datastore.AppPreferences
import com.cmp.community.healers.softskilltraining.core.datastore.AppPreferencesImpl
import com.cmp.community.healers.softskilltraining.data.repository.AuthRepositoryImpl
import com.cmp.community.healers.softskilltraining.data.repository.CandidateRepositoryImpl
import com.cmp.community.healers.softskilltraining.domain.repository.AuthRepository
import com.cmp.community.healers.softskilltraining.domain.repository.CandidateRepository
import org.koin.dsl.module

val repositoryModule = module {
    // DataStore<Preferences> is provided by platformModule
    single<AppPreferences> { AppPreferencesImpl(dataStore = get()) }
    single<AuthRepository> { AuthRepositoryImpl(api = get()) }
    single<CandidateRepository> { CandidateRepositoryImpl(api = get()) }
}