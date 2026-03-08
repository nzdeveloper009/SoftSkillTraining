package com.cmp.community.healers.softskilltraining.di

import com.cmp.community.healers.softskilltraining.core.datastore.DATA_STORE_FILE_NAME
import com.cmp.community.healers.softskilltraining.core.datastore.createDataStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single {
        createDataStore {
            androidContext().filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
        }
    }
}