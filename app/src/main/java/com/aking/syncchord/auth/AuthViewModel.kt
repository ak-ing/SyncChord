package com.aking.syncchord.auth

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.aking.base.Async
import com.aking.base.app
import com.aking.base.base.BaseViewModel
import com.aking.base.widget.logI
import com.aking.syncchord.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class AuthUiState(
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val error: Throwable? = null
)

class AuthViewModel(
    override val initialState: AuthUiState,
    private val authRepository: AuthRepository
) : BaseViewModel<AuthUiState>() {

    /**
     * Holds the current authentication state of the application.
     */
    override fun onInit() {
        authRepository.authState.onEach {
            logI(it.toString())
            when (it) {
                is Async.Loading -> {
                    update { copy(isLoading = true) }
                    delay(1000)
                }

                is Async.Fail -> {
                    val message = app.getString(R.string.text_auth_fail)
                    update {
                        copy(isLoading = false, errorMessage = message, error = it.error)
                    }
                }

                is Async.Uninitialized -> {
                    update { copy(isLoading = false) }
                }

                is Async.Success -> {
                    update { copy(isLoading = false, isAuthenticated = true) }
                }
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Triggers a sign-in flow which will update the [stateFlow].
     */
    fun signIn(context: Context) {
        viewModelScope.launch {
            authRepository.signIn(context)
        }
    }

    /**
     * Attempts to sign in with previously cached credentials.
     *
     * If this doesn't move the [stateFlow] to authenticated, interactive sign in via [signIn] will
     * be required.
     */
    fun signInAutomatically() {
        viewModelScope.launch {
            authRepository.signInWithCachedCredentials()
        }
    }

    suspend fun hasCachedCredentials() = authRepository.hasCachedCredentials()

    fun userMessageShown() {
        update { copy(errorMessage = null, error = null) }
    }
}