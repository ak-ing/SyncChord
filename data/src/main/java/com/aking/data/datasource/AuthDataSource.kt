package com.aking.data.datasource

import android.content.Context
import android.util.Log
import com.aking.data.AuthProvider
import com.aking.data.Convex
import com.aking.data.R
import com.aking.data.mapToAsync
import com.aking.data.model.Async
import com.aking.data.model.Async.Fail
import com.aking.data.model.Async.Success
import com.aking.data.model.Auth0Token
import com.aking.data.model.AuthParam
import com.aking.data.model.Params
import com.aking.data.toAuth0Provider
import com.auth0.android.result.Credentials
import dev.convex.android.AuthState
import dev.convex.android.ConvexClientWithAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform

/**
 * @author Ak
 * 2024/9/29 16:11
 */
class AuthDataSource(private val convex: ConvexClientWithAuth<Credentials>) {

    val authState: Flow<Async<Auth0Token>> = convex.authState.transform { state ->
        if (state is AuthState.Authenticated) {
            val authProvider = state.userInfo.toAuth0Provider()
            signInAuth0(authProvider, state.userInfo).onSuccess {
                Log.v("AuthDataSource", "onEach onSuccess: $it")
                emit(Success(it))
            }.onFailure {
                Log.e("AuthDataSource", "onEach onFailure: $it")
                emit(Fail<Auth0Token>(it, Convex.app.getString(R.string.text_auth_fail)))
            }
        } else {
            emit(state.mapToAsync<Auth0Token>())
        }
    }.flowOn(Dispatchers.IO)

    /**
     * 使用Auth0信息进行登录并返回token
     */
    private suspend fun signInAuth0(authProvider: AuthProvider, userInfo: Credentials): Result<Auth0Token> {
        val param = userInfo.user.run { Params(email, pictureURL, name) }
        val authParam = AuthParam(param, authProvider.provider, authProvider.id)
        Log.v("AuthDataSource", "signInAuth0: $authParam")
        return runCatching {
            convex.mutation<Auth0Token>("auth:signInAuth0", authParam.toArgs())
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