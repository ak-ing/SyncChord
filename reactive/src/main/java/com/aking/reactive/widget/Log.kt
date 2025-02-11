@file:Suppress("NOTHING_TO_INLINE")

package com.aking.reactive.widget

import timber.log.Timber

/**
 * Timber Log拓展
 * @author Ak
 * 2024/8/17 17:41
 */
inline fun logV(message: String?, vararg args: Any?) {
    Timber.v(message, *args)
}

inline fun logD(message: String?, vararg args: Any?) {
    Timber.d(message, *args)
}

inline fun logI(message: String?, vararg args: Any?) {
    Timber.i(message, *args)
}

inline fun logW(message: String?, vararg args: Any?) {
    Timber.w(message, *args)
}

inline fun logE(message: String?, vararg args: Any?) {
    Timber.e(message, *args)
}

inline fun logWTF(message: String?, vararg args: Any?) {
    Timber.wtf(message, *args)
}

inline fun String.logV() = logV(this)

inline fun String.logD() = logD(this)

inline fun String.logI() = logI(this)

inline fun String.logW() = logW(this)

inline fun String.logE() = logE(this)