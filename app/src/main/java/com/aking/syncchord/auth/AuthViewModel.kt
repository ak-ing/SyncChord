package com.aking.syncchord.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aking.data.model.Async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    /**
     * Holds the current authentication state of the application.
     */
    val authState = authRepository.authState.stateIn(
        viewModelScope,
        SharingStarted.Lazily,
        Async.Uninitialized
    )

    /**
     * Triggers a sign-in flow which will update the [authState].
     */
    fun signIn(context: Context) {
        viewModelScope.launch {
            authRepository.signIn(context)
        }
    }

    /**
     * Attempts to sign in with previously cached credentials.
     *
     * If this doesn't move the [authState] to authenticated, interactive sign in via [signIn] will
     * be required.
     */
    fun signInAutomatically() {
        viewModelScope.launch {
            authRepository.signInWithCachedCredentials()
        }
    }
}