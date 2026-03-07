package com.cmp.community.healers.softskilltraining.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

fun initKoin(appDeclaration: KoinApplication.() -> Unit = {}) = startKoin {
    appDeclaration()
    modules(
        platformModule,
        networkModule,
        repositoryModule,
        viewModelModule
    )
}