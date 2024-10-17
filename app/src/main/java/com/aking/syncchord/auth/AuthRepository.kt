package com.aking.syncchord.auth

import android.content.Context
import com.aking.base.Async
import com.aking.base.base.BaseRepository
import com.aking.base.widget.logE
import com.aking.base.widget.logI
import com.aking.base.widget.logV
import com.aking.data.datasource.AuthDataSource
import com.aking.data.model.Auth0Token
import com.aking.data.toAuth0Provider
import dev.convex.android.AuthState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
class AuthRepository(private val dataSource: AuthDataSource) : BaseRepository() {

    val authState: Flow<Async<Auth0Token>> = dataSource.authState.transform { state ->
        if (state is AuthState.Authenticated) {
            val authProvider = state.userInfo.toAuth0Provider()
            dataSource.signInAuth0(authProvider, state.userInfo).onSuccess {
                logV("transform onSuccess: $it")
                emit(Async.Success(it))
            }.onFailure {
                logE("transform onFailure: $it")
                emit(Async.Fail<Auth0Token>(it))
            }
        } else {
            logI(state.toString())
            emit(state.mapToAsync<Auth0Token>())
        }
    }.flowOn(Dispatchers.IO)

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
}