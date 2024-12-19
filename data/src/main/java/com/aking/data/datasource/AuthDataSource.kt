package com.aking.data.datasource

import android.content.Context
import com.aking.data.model.Auth0Token
import com.auth0.android.result.Credentials
import dev.convex.android.ConvexClientWithAuth

/**
 * @author Ak
 * 2024/9/29 16:11
 */
class AuthDataSource(private val convex: ConvexClientWithAuth<Credentials>) {

    val authState get() = convex.authState

    /**
     * 使用Auth0信息进行登录并返回token
     */
    suspend fun signInAuth0(): Result<Auth0Token> {
        return runCatching {
            convex.mutation<Auth0Token>("auth_auth0:signInAuth0")
        }
    }

    suspend fun signIn(context: Context) {
        convex.login(context)
    }

    suspend fun signInWithCachedCredentials() {
        convex.loginFromCache()
    }

    suspend fun logout(context: Context) {
        convex.logout(context)
    }

}