package com.aking.syncchord.auth

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.aking.data.datasource.AuthDataSource
import com.aking.data.model.Auth0Token
import com.aking.reactive.Async
import com.aking.reactive.base.BaseRepository
import com.aking.reactive.widget.logD
import com.aking.reactive.widget.logE
import com.aking.reactive.widget.logI
import com.aking.reactive.widget.logV
import com.aking.syncchord.util.contains
import com.aking.syncchord.util.getData
import com.aking.syncchord.util.remove
import com.aking.syncchord.util.setData
import dev.convex.android.AuthState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform


/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class AuthRepository(
    scope: CoroutineScope,
    private val dataSource: AuthDataSource,
    private val dataStore: DataStore<Preferences>
) : BaseRepository() {

    val authState: Flow<Async<Auth0Token>> = dataSource.authState.transform { state ->
        logI(state.toString())
        if (state is AuthState.Authenticated) {
            // 缓存中存在token
            if (hasCachedCredentials()) {
                emit(Async.Success(dataStore.getData<Auth0Token>(AUTH0_KEY)))
                return@transform
            }
            // 缓存中不存在token，请求服务器
            dataSource.signInAuth0().onSuccess {
                logV("transform signInAuth0 onSuccess: $it")
                dataStore.setData(AUTH0_KEY, it)
                emit(Async.Success(it))
            }.onFailure {
                logE("transform signInAuth0 onFailure: $it")
                emit(Async.Fail<Auth0Token>(it))
            }

        } else {
            logI(state.toString())
            emit(state.mapToAsync<Auth0Token>())
        }
    }.flowOn(Dispatchers.IO).catch {
        it.printStackTrace()
        emit(Async.Fail(it))
    }.stateIn(
        scope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Async.Uninitialized
    )


    /**
     * 将AuthState转换为Async
     */
    private suspend fun <T> AuthState<*>.mapToAsync(): Async<T> {
        return when (this) {
            is AuthState.Unauthenticated -> {
                dataStore.remove(AUTH0_KEY)
                Async.Uninitialized
            }

            is AuthState.AuthLoading -> Async.Loading()
            else -> error("Unknown AuthState")
        }
    }

    /**
     * 判断是否已经缓存了token
     */
    suspend fun hasCachedCredentials(): Boolean {
        val contains = dataStore.contains(AUTH0_KEY)
        logI("hasCachedCredentials: $contains")
        return contains
    }


    suspend fun logout(context: Context) {
        request { dataSource.logout(context) }
    }

    suspend fun signIn(context: Context) {
        request { dataSource.signIn(context) }
    }

    suspend fun signInWithCachedCredentials() {
        if (hasCachedCredentials().not()) {
            logD("signInWithCachedCredentials: no cached credentials")
            return
        }
        request { dataSource.signInWithCachedCredentials() }
    }

    companion object {
        val AUTH0_KEY = stringPreferencesKey("auth0_key")
    }
}