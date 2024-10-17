package com.aking.data

import com.auth0.android.result.Credentials

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
 * 认证方式
 */
data class AuthProvider(val provider: String, val id: String)

