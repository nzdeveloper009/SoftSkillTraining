package com.cmp.community.healers.softskilltraining

import android.app.Application
import com.cmp.community.healers.softskilltraining.di.initKoin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import org.koin.android.ext.koin.androidContext
import java.lang.ref.WeakReference

class AndroidApp : Application() {

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main.immediate)

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AndroidApp)
        }
    }

    override fun onTerminate() {
        coroutineScope.cancel()
        super.onTerminate()
    }

    companion object {
        private lateinit var weakSelf: WeakReference<AndroidApp>
        fun get(): AndroidApp = weakSelf.get()!!
        fun getCoroutineScope() = get().coroutineScope
    }

    init {
        weakSelf = WeakReference(this)
    }
}