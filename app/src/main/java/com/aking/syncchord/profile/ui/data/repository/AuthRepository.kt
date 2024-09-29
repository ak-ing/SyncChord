package com.aking.syncchord.profile.ui.data.repository

import android.content.Context
import com.aking.base.app
import com.aking.base.base.BaseRepository
import com.aking.data.ConvexDataSource
import dev.convex.android.AuthState

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class AuthRepository(private val dataSource: ConvexDataSource) : BaseRepository() {

    val authState get() = dataSource.authState

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        //user = null
    }

    suspend fun logout() {
        request {
            dataSource.logout(app)
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