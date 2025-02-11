package com.aking.syncchord

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.aking.data.Convex
import com.aking.reactive.base.BaseApplication
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androix.startup.KoinStartup.onKoinStartup
import org.koin.core.lazyModules
import org.koin.core.waitAllStartJobs
import org.koin.dsl.lazyModule
import org.koin.mp.KoinPlatform

/**
 * @author Ak
 * 2024/9/10 10:27
 */
class App : BaseApplication() {

    companion object {
        // No need to cancel this scope as it'll be torn down with the process
        val applicationScope =
            CoroutineScope(SupervisorJob() + CoroutineExceptionHandler { _, throwable ->
                throwable.printStackTrace()
            })
    }

    init {
        // Use AndroidX Startup for Koin
        @Suppress("OPT_IN_USAGE")
        onKoinStartup {
            // Log Koin into Android logger
            androidLogger()
            // Reference Android context
            androidContext(this@App)
            // Load modules
            lazyModules(appModule + lazyModule {
                // app startup
                Convex.init(this@App)
            })
        }
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(ProcessLifecycleObserver())

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