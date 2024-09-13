package com.aking.base.widget

import timber.log.Timber

/**
 * Timber Log拓展
 * @author Ak
 * 2024/8/17 17:41
 */
fun logV(message: String?, vararg args: Any?) {
    Timber.v(message, *args)
}

fun logD(message: String?, vararg args: Any?) {
    Timber.d(message, *args)
}

fun logI(message: String?, vararg args: Any?) {
    Timber.i(message, *args)
}

fun logW(message: String?, vararg args: Any?) {
    Timber.w(message, *args)
}

fun logE(message: String?, vararg args: Any?) {
    Timber.e(message, *args)
}

fun logWTF(message: String?, vararg args: Any?) {
    Timber.wtf(message, *args)
}

fun String.logV() = logV(this)

fun String.logD() = logD(this)

fun String.logI() = logI(this)

fun String.logW() = logW(this)

fun String.logE() = logE(this)