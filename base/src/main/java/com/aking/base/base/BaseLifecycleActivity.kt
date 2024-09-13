package com.aking.base.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import timber.log.Timber

/**
 * Created by Rick on 2023-07-08  23:10
 * Description:
 */
abstract class BaseLifecycleActivity : AppCompatActivity() {

    private var lifecycleLog = false

    /**
     * 生命周期日志开关
     */
    protected fun lifecycleLogEnable(enable: Boolean) {
        lifecycleLog = enable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (lifecycleLog) Timber.v("onCreate")
    }

    override fun onStart() {
        super.onStart()
        if (lifecycleLog) Timber.v("onStart")
    }

    override fun onRestart() {
        super.onRestart()
        if (lifecycleLog) Timber.v("onRestart")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (lifecycleLog) Timber.v("onSaveInstanceState")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (lifecycleLog) Timber.v("onNewIntent")
    }

    override fun onResume() {
        super.onResume()
        if (lifecycleLog) Timber.v("onResume")
    }

    override fun onPause() {
        super.onPause()
        if (lifecycleLog) Timber.v("onPause")
    }

    override fun onStop() {
        super.onStop()
        if (lifecycleLog) Timber.v("onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (lifecycleLog) Timber.v("onDestroy")
    }


}