package com.aking.syncchord.auth

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.aking.data.model.Auth0Token
import com.aking.reactive.Async
import com.aking.reactive.base.BaseAndroidViewModel
import com.aking.reactive.base.Intent
import com.aking.reactive.base.Reducer
import com.aking.reactive.widget.logD
import com.aking.syncchord.R
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

data class AuthState(
    val auth: Async<Auth0Token> = Async.Uninitialized,
    val errorMessage: String? = null
)


sealed class AuthAction : Intent {
    data class SignIn(val activity: Activity) : AuthAction()
    data object SignInAutomatically : AuthAction()
    data object UserMessageShown : AuthAction()
}

class AuthViewModel(
    app: Application,
    private val authRepository: AuthRepository
) : BaseAndroidViewModel<AuthState>(app, AuthState()), Reducer<AuthAction> {

    /**
     * Holds the current authentication state of the application.
     */
    override fun onInitialize() {
        authRepository.authState.onEach {
            logD(it.toString())
            handleAuthState(it)
        }.launchIn(viewModelScope)
    }


    override fun reducer(intent: AuthAction) {
        when (intent) {
            is AuthAction.SignIn -> signIn(intent.activity)
            AuthAction.SignInAutomatically -> signInAutomatically()
            AuthAction.UserMessageShown -> userMessageShown()
        }
    }

    private fun handleAuthState(state: Async<Auth0Token>) {
        when (state) {
            is Async.Loading -> {
                update { copy(auth = state) }
            }

            is Async.Fail -> {
                val message = getAppContext().getString(R.string.text_auth_fail)
                update {
                    copy(auth = state, errorMessage = message)
                }
            }

            is Async.Uninitialized, is Async.Success -> {
                update { copy(auth = state) }
            }
        }
    }

    /**
     * Triggers a sign-in flow which will update the [stateFlow].
     */
    private fun signIn(context: Context) {
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
    private fun signInAutomatically() {
        viewModelScope.launch {
            authRepository.signInWithCachedCredentials()
        }
    }

    /**
     * Clears the error message to dismiss the current one.
     */
    private fun userMessageShown() {
        update { copy(auth = Async.Uninitialized, errorMessage = null) }
    }
}