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
import com.aking.data.AuthProvider
import com.aking.data.datasource.AuthDataSource
import com.aking.data.model.StoreParam
import com.aking.data.model.Tokens
import com.aking.data.toAuth0Provider
import com.aking.syncchord.util.contains
import com.aking.syncchord.util.get
import com.aking.syncchord.util.getData
import com.aking.syncchord.util.remove
import com.aking.syncchord.util.setData
import com.auth0.android.result.Credentials
import dev.convex.android.AuthState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.transform

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class AuthRepository(
    private val dataSource: AuthDataSource,
    private val dataStore: DataStore<Preferences>
) : BaseRepository() {

    val authState: Flow<Async<Tokens>> = dataSource.authState.transform { state ->
        if (state is AuthState.Authenticated) {
            // 缓存中存在token
            if (dataStore.contains(AUTH0_KEY)) {
                emit(Async.Success(dataStore.getData<Tokens>(AUTH0_KEY)))
                return@transform
            }
            // 缓存中不存在token，请求服务器
            val authProvider = state.userInfo.toAuth0Provider()
            sigInAuth0WithToken(authProvider, state.userInfo).onSuccess {
                logV("transform sigInAuth0WithToken onSuccess: $it")
                dataStore.setData(AUTH0_KEY, it)
                emit(Async.Success(it))
            }.onFailure {
                logE("transform sigInAuth0WithToken onFailure: $it")
                emit(Async.Fail<Tokens>(it))
            }
        } else {
            logI(state.toString())
            emit(state.mapToAsync<Tokens>())
        }
    }.flowOn(Dispatchers.IO)


    private suspend fun sigInAuth0WithToken(
        authProvider: AuthProvider,
        userInfo: Credentials
    ): Result<Tokens> {
        val auth0Token = dataSource.signInAuth0(authProvider, userInfo).getOrElse {
            return Result.failure(it)
        }
        return store(StoreParam(refreshToken = auth0Token.refreshToken))
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
        dataSource.validateSession(dataStore.get(AUTH0_KEY, "").split("|").last()).onEach {
            // 验证失败，清除缓存
            if (it.getOrNull() == false) {
                dataStore.remove(AUTH0_KEY)
            }
        }
    }

    suspend fun store(args: StoreParam) = request {
        dataSource.store(args)
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