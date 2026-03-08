package com.cmp.community.healers.softskilltraining.di

import com.cmp.community.healers.softskilltraining.core.datastore.DATA_STORE_FILE_NAME
import com.cmp.community.healers.softskilltraining.core.datastore.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual val platformModule: Module = module {
    single {
        createDataStore {
            val docDir = NSFileManager.defaultManager.URLForDirectory(
                directory   = NSDocumentDirectory,
                inDomain    = NSUserDomainMask,
                appropriateForURL = null,
                create      = false,
                error       = null
            )!!.path!!
            "$docDir/$DATA_STORE_FILE_NAME"
        }
    }
}