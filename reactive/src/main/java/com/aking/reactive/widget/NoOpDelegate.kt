package com.aking.reactive.widget

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

/**
 * 空实现委托
 * @author Ak
 * 2024/10/29 9:36
 */
inline fun <reified T : Any> noOpDelegate(): T {
    val javaClass = T::class.java
    return Proxy.newProxyInstance(
        javaClass.classLoader, arrayOf(javaClass), NO_OP_HANDLER
    ) as T
}

val NO_OP_HANDLER = InvocationHandler { _, _, _ ->
    // no op
}
