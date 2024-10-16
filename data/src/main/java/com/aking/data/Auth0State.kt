package com.aking.data

import com.aking.data.model.Async
import com.aking.data.model.Async.Loading
import com.aking.data.model.Async.Uninitialized
import com.auth0.android.result.Credentials
import dev.convex.android.AuthState

/**
 * @author Ak
 * 2024/9/29 17:51
 */

/**
 * 将Credentials转换为AuthProvider
 */
fun Credentials.toAuth0Provider(): AuthProvider {
    val providerInfo = user.getExtraInfo()["sub"].toString().split("|")
    val provider = if (providerInfo.first() == "auth0") {
        "password"
    } else {
        providerInfo.first()
    }
    return AuthProvider(provider, providerInfo.last())
}

/**
 * 将AuthState转换为Async
 */
fun <T> AuthState<*>.mapToAsync(): Async<T> {
    return when (this) {
        is AuthState.Unauthenticated -> Uninitialized
        is AuthState.AuthLoading -> Loading()
        else -> error("Unknown AuthState")
    }
}

/**
 * 认证方式
 */
data class AuthProvider(val provider: String, val id: String)

