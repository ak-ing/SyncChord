package com.aking.syncchord.auth

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.viewModelScope
import com.aking.base.Async
import com.aking.base.base.BaseAndroidViewModel
import com.aking.base.base.Intent
import com.aking.base.base.Reducer
import com.aking.base.widget.logI
import com.aking.data.model.Auth0Token
import com.aking.syncchord.R
import kotlinx.coroutines.delay
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
            logI(it.toString())
            when (it) {
                is Async.Loading -> {
                    update { copy(auth = it) }
                    delay(1000)
                }

                is Async.Fail -> {
                    val message = getAppContext().getString(R.string.text_auth_fail)
                    update {
                        copy(auth = it, errorMessage = message)
                    }
                }

                is Async.Uninitialized, is Async.Success -> {
                    update { copy(auth = it) }
                }
            }
        }.launchIn(viewModelScope)
    }


    override fun reducer(intent: AuthAction) {
        when (intent) {
            is AuthAction.SignIn -> signIn(intent.activity)
            AuthAction.SignInAutomatically -> signInAutomatically()
            AuthAction.UserMessageShown -> userMessageShown()
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