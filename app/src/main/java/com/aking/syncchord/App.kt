package com.aking.syncchord

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.aking.base.base.BaseApplication
import com.aking.data.Convex
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup.onKoinStartup
import org.koin.core.lazyModules
import org.koin.core.waitAllStartJobs
import org.koin.mp.KoinPlatform

/**
 * @author Ak
 * 2024/9/10 10:27
 */
class App : BaseApplication() {

    init {
        // Use AndroidX Startup for Koin
        @Suppress("OPT_IN_USAGE")
        onKoinStartup {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@App)
            // Load modules
            lazyModules(appModule)
        }
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(ProcessLifecycleObserver())
        Convex.init(this)

        val koin = KoinPlatform.getKoin()
        // wait for start completion
        koin.waitAllStartJobs()
    }

    class ProcessLifecycleObserver : DefaultLifecycleObserver {
        /**
         * 当应用程序退到后台时
         */
        override fun onStop(owner: LifecycleOwner) {}
    }
}