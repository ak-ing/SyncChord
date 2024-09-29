package com.aking.syncchord.profile.ui.login

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.aking.syncchord.R
import com.aking.syncchord.profile.ui.data.repository.AuthRepository
import com.aking.syncchord.profile.ui.ui.login.LoginFormState
import kotlinx.coroutines.launch


class LoginViewModel(private val authRepository: AuthRepository, application: Application) :
    AndroidViewModel(application) {
    /**
     * Holds the current authentication state of the application.
     */
    val authState get() = authRepository.authState

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    /**
     * Triggers a sign-in flow which will update the [authState].
     */
    fun signIn() {
        viewModelScope.launch {
            authRepository.signIn(getApplication())
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

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }
}