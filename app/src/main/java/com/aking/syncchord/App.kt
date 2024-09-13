package com.aking.syncchord

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.aking.base.base.BaseApplication

/**
 * @author Ak
 * 2024/9/10 10:27
 */
class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(ProcessLifecycleObserver())
    }

    class ProcessLifecycleObserver : DefaultLifecycleObserver {
        /**
         * 当应用程序退到后台时
         */
        override fun onStop(owner: LifecycleOwner) {}
    }
}