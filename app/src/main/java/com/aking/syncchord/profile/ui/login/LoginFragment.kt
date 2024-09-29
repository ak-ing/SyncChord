package com.aking.syncchord.profile.ui.login

import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.widget.doAfterTextChanged
import com.aking.base.base.BaseFragment
import com.aking.syncchord.R
import com.aking.syncchord.databinding.FragmentLoginBinding
import com.aking.syncchord.profile.ui.ui.login.LoggedInUserView
import org.koin.androidx.viewmodel.ext.android.viewModel


class LoginFragment : BaseFragment<FragmentLoginBinding>(R.layout.fragment_login) {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun FragmentLoginBinding.initView() {
        val doAfterTextChanged = username.doAfterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }
        password.addTextChangedListener(doAfterTextChanged)
        password.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loginViewModel.signIn()
            }
            false
        }

        login.setOnClickListener {
            loading.visibility = View.VISIBLE
            loginViewModel.signIn()
        }
    }

    override fun FragmentLoginBinding.initData() {
        loginViewModel.loginFormState.observe(viewLifecycleOwner) { loginFormState ->
            if (loginFormState == null) {
                return@observe
            }
            login.isEnabled = loginFormState.isDataValid
            loginFormState.usernameError?.let {
                username.error = getString(it)
            }
            loginFormState.passwordError?.let {
                password.error = getString(it)
            }
        }

        //loginViewModel.loginResult.observe(viewLifecycleOwner, Observer { loginResult ->
        //    loginResult ?: return@Observer
        //    loading.visibility = View.GONE
        //    loginResult.error?.let {
        //        showLoginFailed(it)
        //    }
        //    loginResult.success?.let {
        //        updateUiWithUser(it)
        //    }
        //})
    }

    private fun updateUiWithUser(model: LoggedInUserView) {
        val welcome = getString(R.string.welcome) + model.displayName
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }
}