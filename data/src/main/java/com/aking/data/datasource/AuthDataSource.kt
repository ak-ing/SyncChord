package com.aking.data.datasource

import android.content.Context
import android.util.Log
import com.aking.data.AuthProvider
import com.aking.data.model.Auth0Token
import com.aking.data.model.AuthParam
import com.aking.data.model.Params
import com.aking.data.model.SignParam
import com.aking.data.model.UserModel
import com.auth0.android.result.Credentials
import dev.convex.android.ConvexClient
import dev.convex.android.ConvexClientWithAuth
import dev.convex.android.MobileConvexClientInterface

/**
 * @author Ak
 * 2024/9/29 16:11
 */
class AuthDataSource(private val convex: ConvexClientWithAuth<Credentials>) {

    val authState get() = convex.authState

    suspend fun setAuth(idToken: String) {
        val field = ConvexClient::class.java.getDeclaredField("ffiClient")
        field.isAccessible = true
        val ffiClient = field.get(convex) as MobileConvexClientInterface
        ffiClient.setAuth(idToken)
    }

    /**
     * 使用Auth0信息进行登录并返回token
     */
    suspend fun signInAuth0(authProvider: AuthProvider, userInfo: Credentials): Result<Auth0Token> {
        val param = userInfo.user.run { Params(email, pictureURL, name) }
        val authParam = AuthParam(param, authProvider.provider, authProvider.id)
        Log.v("AuthDataSource", "signInAuth0: $authParam")
        return runCatching {
            convex.mutation<Auth0Token>("auth:signInAuth0", authParam.toArgs())
        }
    }

    suspend fun auth0RetrieveUserInfo(param: SignParam): Result<UserModel> {
        return runCatching {
            convex.mutation<UserModel>("auth:auth0RetrieveUserInfo", param.toArgs())
        }
    }

    /**
     * 验证session是否有效
     */
    suspend fun validateSession(session: String) = convex.subscribe<Boolean>(
        "auth:validateSession", mapOf(
            "token" to session
        )
    )

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