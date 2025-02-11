package com.aking.reactive

import android.app.Application


/**
 * 获取Application
 */
val app get() :Application = AppGlobe.get()

/**
 * Created by AK on 2023-01-30  21:12.
 * Description: 全局ApplicationContext
 */
object AppGlobe {
    private lateinit var context: Application

    internal fun init(application: Application) {
        context = application
    }

    /**
     * 获取Application
     */
    fun get(): Application {
        if (!this::context.isInitialized) {
            error("AppGlobal context is not initialize.")
        }
        return context
    }

}