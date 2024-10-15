package com.aking.data.datasource

import android.content.Context
import android.util.Log
import com.aking.data.AuthProvider
import com.aking.data.model.Auth0Token
import com.aking.data.model.AuthParam
import com.aking.data.model.Params
import com.aking.data.toAuth0Provider
import com.auth0.android.result.Credentials
import dev.convex.android.AuthState
import dev.convex.android.ConvexClientWithAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach

/**
 * @author Ak
 * 2024/9/29 16:11
 */
class AuthDataSource(private val convex: ConvexClientWithAuth<Credentials>) {

    val authState = convex.authState.onEach { state ->
        if (state is AuthState.Authenticated) {
            val authProvider = state.userInfo.toAuth0Provider()
            val auth0Token = signInAuth0(authProvider, state.userInfo)
            Log.i("AuthDataSource", "onEach signInAuth0: $auth0Token")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 使用Auth0信息进行登录并返回token
     */
    private suspend fun signInAuth0(authProvider: AuthProvider, userInfo: Credentials): Auth0Token {
        val param = userInfo.user.run { Params(email, pictureURL, name) }
        val authParam = AuthParam(param, authProvider.provider, authProvider.id)
        Log.i("AuthDataSource", "signInAuth0: $authParam")
        return convex.mutation<Auth0Token>("auth:signInAuth0", authParam.toArgs())
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