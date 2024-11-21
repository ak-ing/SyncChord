package com.aking.syncchord.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.aking.base.Async
import com.aking.base.base.BaseRepository
import com.aking.base.widget.logE
import com.aking.base.widget.logI
import com.aking.base.widget.logV
import com.aking.data.datasource.AuthDataSource
import com.aking.data.model.Auth0Token
import com.aking.data.model.SignParam
import com.aking.data.toAuth0Provider
import com.aking.syncchord.util.Jwt
import com.aking.syncchord.util.contains
import com.aking.syncchord.util.getData
import com.aking.syncchord.util.remove
import com.aking.syncchord.util.setData
import com.auth0.android.result.Credentials
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.convex.android.AuthState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.declaredMemberProperties


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class AuthRepository(
    private val dataSource: AuthDataSource,
    private val dataStore: DataStore<Preferences>
) : BaseRepository() {

    val authState: Flow<Async<Auth0Token>> = dataSource.authState.transform { state ->
        if (state is AuthState.Authenticated) {
            // 缓存中存在token
            if (dataStore.contains(AUTH0_KEY)) {
                emit(Async.Success(dataStore.getData<Auth0Token>(AUTH0_KEY)))
                return@transform
            }
            // 缓存中不存在token，请求服务器
            val authProvider = state.userInfo.toAuth0Provider()
            dataSource.signInAuth0(authProvider, state.userInfo).onSuccess {
                logV("transform signInAuth0 onSuccess: $it")
                dataStore.setData(AUTH0_KEY, it)
                val state = dataSource.authState
                val data = state.value
                if (data is AuthState.Authenticated) {
                    val jwt = Jwt(data.userInfo.idToken)
                    jwt.subject = it.token
                    val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
                    keyPairGenerator.initialize(2048)
                    val keyPair = keyPairGenerator.generateKeyPair()
                    val rsaPrivateKey = keyPair.private as RSAPrivateKey
                    val rsaPublicKey = keyPair.public as RSAPublicKey
                    val algorithm = Algorithm.RSA256(rsaPublicKey, rsaPrivateKey)
                    val token = JWT.create()
                        .withIssuer(jwt.issuer)
                        .sign(algorithm)
                    logI(token)

                    if (state is MutableStateFlow) {
                        data.userInfo.apply {
                            val copy = data.copy(Credentials(token, accessToken, type, refreshToken, expiresAt, scope))
                            state.emit(copy)
                            logI("idToken: ${copy.userInfo.idToken}")
                        }
                    }
                }
                emit(Async.Success(it))
            }.onFailure {
                logE("transform signInAuth0 onFailure: $it")
                emit(Async.Fail<Auth0Token>(it))
            }

        } else {
            logI(state.toString())
            emit(state.mapToAsync<Auth0Token>())
        }
    }.flowOn(Dispatchers.IO)

    private fun setIdToken(data: Any, token: String) {
        val dataClass = data::class
        val idTokenProperty = dataClass.declaredMemberProperties
            .find { it.name == "idToken" && it is KMutableProperty1<*, *> } as? KMutableProperty1<Any, String>

        if (idTokenProperty != null) {
            idTokenProperty.set(data, token)
        } else {
            // 处理 idToken 属性不可访问或不可修改的情况
            println("idToken property is not accessible or not mutable")
        }
    }

    /**
     * 将AuthState转换为Async
     */
    private fun <T> AuthState<*>.mapToAsync(): Async<T> {
        return when (this) {
            is AuthState.Unauthenticated -> Async.Uninitialized
            is AuthState.AuthLoading -> Async.Loading()
            else -> error("Unknown AuthState")
        }
    }

    /**
     * 判断是否已经缓存了token
     */
    suspend fun hasCachedCredentials() = dataStore.contains(AUTH0_KEY)

    /**
     * 验证session
     */
    suspend fun validateSession() = request {
        val auth0Token = dataStore.getData<Auth0Token>(AUTH0_KEY)
        dataSource.validateSession(auth0Token.token).onEach {
            // 验证失败，清除缓存
            if (it.getOrNull() == false) {
                dataStore.remove(AUTH0_KEY)
            }
        }
    }

    suspend fun auth0RetrieveUserInfo(param: SignParam) = request {
        dataSource.auth0RetrieveUserInfo(param)
    }

    suspend fun logout(context: Context) {
        request {
            dataSource.logout(context)
        }
    }

    suspend fun signIn(context: Context) {
        request {
            dataSource.signIn(context)
        }
    }

    suspend fun signInWithCachedCredentials() {
        request {
            dataSource.signInWithCachedCredentials()
        }
    }

    companion object {
        val AUTH0_KEY = stringPreferencesKey("auth0_key")
    }
}